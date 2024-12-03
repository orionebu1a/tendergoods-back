package recommendationSystem

import IntegrationTest
import com.orion.enums.BidState
import com.orion.filter.BidPageFilter
import com.orion.model.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals

class RecommendationSystemTest : IntegrationTest() {
    @Test
    fun recommendationTest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val tokenList = tokenLoginHelper()
        val tokenMy = tokenList[0]
        val tokenOtherSeller = tokenList[1]
        val tokenSeller3 = tokenList[2]
        val tokenUser = tokenList[3]

        val responseItem1 = client.post("/items") {
            contentType(ContentType.Application.Json)
            bearerAuth(tokenMy)
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

        val responseItem2 = client.post("/items") {
            contentType(ContentType.Application.Json)
            bearerAuth(tokenMy)
            setBody(
                ItemForm(
                    title = "Мой Товар 2",
                    description = "",
                    totalAmount = 5,
                    categoryId = 1,
                    imageUrl = ""
                )
            )
        }

        val item1 = Json.decodeFromString<ItemDto>(responseItem1.bodyAsText())
        val item2 = Json.decodeFromString<ItemDto>(responseItem2.bodyAsText())

        val responseBid1 = client.post("/bids") {
            contentType(ContentType.Application.Json)
            bearerAuth(tokenMy)
            setBody(
                BidForm(
                    startingPrice = 100.0,
                    priceIncrement = 20.0,
                    location = "Moscow",
                    latitude = 55.0,
                    longitude = 35.0,
                    startTime = Instant.now(),
                    endTime = Instant.now().plusMillis(1000),
                    items = listOf(item1.id, item2.id),
                )
            )
        }
        val bid1 = Json.decodeFromString<BidDto>(responseBid1.bodyAsText())

        val responseItem3 = client.post("/items") {
            contentType(ContentType.Application.Json)
            bearerAuth(tokenOtherSeller)
            setBody(
                ItemForm(
                    title = "Его Товар 1",
                    description = "",
                    totalAmount = 2,
                    categoryId = 2,
                    imageUrl = ""
                )
            )
        }

        val responseItem4 = client.post("/items") {
            contentType(ContentType.Application.Json)
            bearerAuth(tokenOtherSeller)
            setBody(
                ItemForm(
                    title = "Его Товар 2",
                    description = "",
                    totalAmount = 5,
                    categoryId = 2,
                    imageUrl = ""
                )
            )
        }

        val item3 = Json.decodeFromString<ItemDto>(responseItem3.bodyAsText())
        val item4 = Json.decodeFromString<ItemDto>(responseItem4.bodyAsText())

        val responseBid2 = client.post("/bids") {
            contentType(ContentType.Application.Json)
            bearerAuth(tokenOtherSeller)
            setBody(
                BidForm(
                    startingPrice = 100.0,
                    priceIncrement = 20.0,
                    location = "Moscow",
                    latitude = 55.0,
                    longitude = 35.0,
                    startTime = Instant.now(),
                    endTime = Instant.now().plusMillis(1000),
                    items = listOf(item3.id, item4.id),
                )
            )
        }
        val bid2 = Json.decodeFromString<BidDto>(responseBid2.bodyAsText())

        val responseItem5 = client.post("/items") {
            contentType(ContentType.Application.Json)
            bearerAuth(tokenSeller3)
            setBody(
                ItemForm(
                    title = "Его Товар 1",
                    description = "",
                    totalAmount = 2,
                    categoryId = 3,
                    imageUrl = ""
                )
            )
        }

        val item5 = Json.decodeFromString<ItemDto>(responseItem3.bodyAsText())

        val responseBid3 = client.post("/bids") {
            contentType(ContentType.Application.Json)
            bearerAuth(tokenSeller3)
            setBody(
                BidForm(
                    startingPrice = 100.0,
                    priceIncrement = 20.0,
                    location = "Moscow",
                    latitude = 55.0,
                    longitude = 35.0,
                    startTime = Instant.now(),
                    endTime = Instant.now().plusMillis(1000),
                    items = listOf(item5.id),
                )
            )
        }
        val bid3 = Json.decodeFromString<BidDto>(responseBid3.bodyAsText())

        val bidsBeforePromotion = client.post("/bids/paged") {
            contentType(ContentType.Application.Json)
            bearerAuth(tokenUser)
            setBody(
                BidPageFilter(
                    from = 0,
                    to = 10,
                    state = BidState.ALL,
                )
            )
        }

        val firstBidAfterPromotion = Json.decodeFromString<List<BidDto>>(bidsBeforePromotion.bodyAsText())
            .map { it.id }

        assertEquals(listOf(bid3.id, bid2.id, bid1.id), firstBidAfterPromotion)
    }
}