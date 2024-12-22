package chat

import IntegrationTest
import com.orion.model.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant

class ChatTest : IntegrationTest() {
    @Test
    fun baseChatTest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val tokenList = tokenLoginHelper()
        val token1 = tokenList[0]
        val token2 = tokenList[1]

        val responseItem1 = client.post("/items") {
            contentType(ContentType.Application.Json)
            bearerAuth(token2)
            setBody(
                ItemForm(
                    title = "Мой Товар 1",
                    description = "",
                    totalAmount = 2,
                    categoryId = 1,
                    imageUrl = ""
                )
            )
        }

        val item1 = Json.decodeFromString<ItemDto>(responseItem1.bodyAsText())

        val responseBid1 = client.post("/bids") {
            contentType(ContentType.Application.Json)
            bearerAuth(token2)
            setBody(
                BidForm(
                    startingPrice = 100.0,
                    priceIncrement = 20.0,
                    location = "Moscow",
                    latitude = 55.0,
                    longitude = 35.0,
                    startTime = Instant.now(),
                    endTime = Instant.now().plusMillis(2000),
                    items = listOf(item1.id),
                )
            )
        }
        val bid1 = Json.decodeFromString<BidDto>(responseBid1.bodyAsText())
        val responseBet = client.post("/bets/doBet") {
            contentType(ContentType.Application.Json)
            bearerAuth(token1)
            setBody(
                BetForm(
                    newPrice = 100.0 + 20.0,
                    bidId = bid1.id,
                )
            )
        }
        Assertions.assertEquals(HttpStatusCode.OK, responseBet.status)
        println(responseBet.bodyAsText())
        runBlocking {
            delay(7000)
        }

        val sendMessageResponse = client.post("/chats/sendMessage") {
            contentType(ContentType.Application.Json)
            bearerAuth(token1)
            setBody(
                MessageForm(
                    bidId = bid1.id,
                    receiverId = user2!!.id.value,
                    text = "Привет, как дела?"
                )
            )
        }
        Assertions.assertEquals(HttpStatusCode.OK, sendMessageResponse.status)

        val replyMessageResponse = client.post("/chats/sendMessage") {
            contentType(ContentType.Application.Json)
            bearerAuth(token2)
            setBody(
                MessageForm(
                    bidId = bid1.id,
                    receiverId = user1!!.id.value,
                    text = "Привет! Все отлично, спасибо!"
                )
            )
        }
        Assertions.assertEquals(HttpStatusCode.OK, replyMessageResponse.status)

        // User 1 retrieves the list of all their chats
        val allChatsResponse = client.get("/chats/allUserChats") {
            bearerAuth(token1)
        }
        Assertions.assertEquals(HttpStatusCode.OK, allChatsResponse.status)

        val allChats = Json.decodeFromString<ChatsDto>(allChatsResponse.bodyAsText())
        Assertions.assertEquals(1, allChats.chats.size)
        Assertions.assertEquals(1, allChats.chats.first().first)

        val chatHistoryResponse = client.get("/chats/${allChats.chats.first().first}") {
            bearerAuth(token1)
        }
        Assertions.assertEquals(HttpStatusCode.OK, chatHistoryResponse.status)

        val chatHistory = Json.decodeFromString<List<MessageDto>>(chatHistoryResponse.bodyAsText())
        Assertions.assertEquals(2, chatHistory.size)
        Assertions.assertEquals("Привет, как дела?", chatHistory[1].text)
        Assertions.assertEquals("Привет! Все отлично, спасибо!", chatHistory[0].text)

    }
}