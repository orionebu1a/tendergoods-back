package moneyTransactionTest

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

class MoneyTransactionTest : IntegrationTest() {
    @Test
    fun moneyTransactionTest() = testApplication {
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

        val responseUser1 = client.get("/users/" + user1!!.id) {
            contentType(ContentType.Application.Json)
            bearerAuth(token1)
        }

        val updatedUser1 = Json.decodeFromString<UserDto>(responseUser1.bodyAsText())
        Assertions.assertEquals(updatedUser1.walletBalance, user1!!.walletBalance - 120.0)

        val responseUser2 = client.get("/users/" + user2!!.id) {
            contentType(ContentType.Application.Json)
            bearerAuth(token1)
        }

        val updatedUser2 = Json.decodeFromString<UserDto>(responseUser1.bodyAsText())
//        Assertions.assertEquals(updatedUser1.walletBalance, user2!!.walletBalance + 120.0)
//        TODO
    }
}