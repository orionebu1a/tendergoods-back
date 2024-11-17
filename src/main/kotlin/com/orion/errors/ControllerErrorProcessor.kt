package com.orion.errors

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.respondWithErrorProcessing(result: ResultWithError<*>) {
    when (result) {
        is ResultWithError.Success<*> -> {
            if (result.data == null) {
                respond(HttpStatusCode.NoContent)
            } else {
                respond(HttpStatusCode.OK, result.data)
            }
        }
        is ResultWithError.Failure -> {
            when (val error = result.error) {
                ServiceError.NotFound -> respond(HttpStatusCode.NotFound, "Resource not found")
                ServiceError.NotOwn -> respond(HttpStatusCode.Forbidden, "Not own resource")
                is ServiceError.Custom -> respond(HttpStatusCode.BadRequest, error.message)
                is ServiceError.DatabaseError -> respond(HttpStatusCode.InternalServerError, error.message)
                ServiceError.InvalidData -> respond(HttpStatusCode.BadRequest, error.message)
                ServiceError.NotAuthorized -> respond(HttpStatusCode.Unauthorized, error.message)
            }
        }
    }
}