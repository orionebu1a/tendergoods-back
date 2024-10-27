import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import com.orion.filter.BidPageFilter
import com.orion.model.BidForm
import com.orion.service.BidService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bidRouting(bidService: BidService) {
    route("/bids") {

        get("paged") {
            val bidPageFilter = call.receive<BidPageFilter>()
            val bids = bidService.findPagedByFilter(bidPageFilter)
            call.respond(bids)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
                return@get
            }

            when (val result = bidService.findById(id)) {
                is ResultWithError.Success -> call.respond(result.data)
                is ResultWithError.Failure -> {
                    when (val error = result.error) {
                        ServiceError.NotFound -> call.respond(HttpStatusCode.NotFound, error.message)
                        is ServiceError.DatabaseError -> call.respond(HttpStatusCode.InternalServerError, error.message)
                        else -> call.respond(HttpStatusCode.BadRequest, error.message)
                    }
                }
            }
        }

        post {
            val bid = call.receive<BidForm>()

            when (val result = bidService.create(bid, call.principal<User>()!!)) {
                is ResultWithError.Success -> call.respond(HttpStatusCode.Created, result.data)
                is ResultWithError.Failure -> {
                    val error = result.error
                    call.respond(
                        when (error) {
                            is ServiceError.DatabaseError -> HttpStatusCode.InternalServerError
                            else -> HttpStatusCode.BadRequest
                        },
                        error.message
                    )
                }
            }
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
                return@put
            }

            val bidForm = call.receive<BidForm>()

            when (val result = bidService.update(id, bidForm, call.principal<User>()!!)) {
                is ResultWithError.Success -> call.respond(HttpStatusCode.OK, result.data)
                is ResultWithError.Failure -> {
                    val error = result.error
                    when (error) {
                        ServiceError.NotFound -> call.respond(HttpStatusCode.NotFound, error.message)
                        ServiceError.NotOwn -> call.respond(HttpStatusCode.Forbidden, error.message)
                        is ServiceError.DatabaseError -> call.respond(HttpStatusCode.InternalServerError, error.message)
                        else -> call.respond(HttpStatusCode.BadRequest, error.message)
                    }
                }
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
                return@delete
            }

            val principal = call.principal<User>()
            if (principal == null) {
                call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
                return@delete
            }

            when (val result = bidService.delete(id, principal)) {
                is ResultWithError.Success -> call.respond(HttpStatusCode.NoContent)
                is ResultWithError.Failure -> {
                    val error = result.error
                    when (error) {
                        ServiceError.NotFound -> call.respond(HttpStatusCode.NotFound, error.message)
                        ServiceError.NotOwn -> call.respond(HttpStatusCode.Forbidden, error.message)
                        is ServiceError.DatabaseError -> call.respond(HttpStatusCode.InternalServerError, error.message)
                        else -> call.respond(HttpStatusCode.BadRequest, error.message)
                    }
                }
            }
        }

    }
}