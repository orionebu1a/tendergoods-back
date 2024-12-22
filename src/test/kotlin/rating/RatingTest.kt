package rating

import IntegrationTest
import com.orion.entity.Promotion
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
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals

class RatingTest : IntegrationTest() {
    @Test
    fun baseRatingTest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val tokenList = tokenLoginHelper()
        val token1 = tokenList[0]
        val token2 = tokenList[1]
        val token3 = tokenList[2]
        val token4 = tokenList[2]

        val responseItem1 = client.post("/items") {
            contentType(ContentType.Application.Json)
            bearerAuth(token1)
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
            bearerAuth(token1)
            setBody(
                BidForm(
                    startingPrice = 100.0,
                    priceIncrement = 20.0,
                    location = "Moscow",
                    latitude = 55.0,
                    longitude = 35.0,
                    startTime = Instant.now(),
                    endTime = Instant.now().plusMillis(1000),
                    items = listOf(item1.id),
                )
            )
        }
        val bid1 = Json.decodeFromString<BidDto>(responseBid1.bodyAsText())

        val responseItem2 = client.post("/items") {
            contentType(ContentType.Application.Json)
            bearerAuth(token2)
            setBody(
                ItemForm(
                    title = "Его Товар 1",
                    description = "",
                    totalAmount = 2,
                    categoryId = 1,
                    imageUrl = ""
                )
            )
        }

        val item2 = Json.decodeFromString<ItemDto>(responseItem2.bodyAsText())

        val responseBid2 = client.post("/bids") {
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
                    endTime = Instant.now().plusMillis(1000),
                    items = listOf(item2.id),
                )
            )
        }
        val bid2 = Json.decodeFromString<BidDto>(responseBid2.bodyAsText())

        val bids = client.post("/bids/paged") {
            contentType(ContentType.Application.Json)
            bearerAuth(token3)
            setBody(
                BidPageFilter(
                    from = 0,
                    to = 10,
                    state = BidState.ALL,
                )
            )
        }

        val decodedBids = Json.decodeFromString<List<BidDto>>(bids.bodyAsText())
            .map { it.id }
        assertEquals(decodedBids, listOf(bid2.id, bid1.id))
        //Изначально первым должен быть последний опубликованный лот

        val reviewResponse = client.post("/reviews") {
            contentType(ContentType.Application.Json)
            bearerAuth(token4)
            setBody(
                ReviewForm(
                    reviewed = 1,
                    rating = 5.0,
                    reviewText = "Хороший продавец"
                )
            )
        }
        assertEquals(reviewResponse.status, HttpStatusCode.OK)

        val bidsAfterReview = client.post("/bids/paged") {
            contentType(ContentType.Application.Json)
            bearerAuth(token3)
            setBody(
                BidPageFilter(
                    from = 0,
                    to = 10,
                    state = BidState.ALL,
                )
            )
        }

        val decodedAfterReview = Json.decodeFromString<List<BidDto>>(bidsAfterReview.bodyAsText())
            .map { it.id }
        assertEquals(decodedAfterReview, listOf(bid1.id, bid2.id))
    }
}