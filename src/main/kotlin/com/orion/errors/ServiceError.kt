package com.orion.errors

sealed class ServiceError(override val message: String) : Throwable(message) {
    data object NotFound : ServiceError("Entity not found")
    data object NotOwn : ServiceError("You should own entity to perform this action")
    data object NotAuthorized : ServiceError("You are not authorized to perform this action")
    data object InvalidData : ServiceError("Invalid data provided")
    data class DatabaseError(val details: String) : ServiceError("Database error: $details")
    data class Custom(val details: String) : ServiceError(details)
}