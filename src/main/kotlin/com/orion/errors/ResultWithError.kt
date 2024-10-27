package com.orion.errors

sealed class ResultWithError<out T> {
    data class Success<out T>(val data: T) : ResultWithError<T>()
    data class Failure(val error: ServiceError) : ResultWithError<Nothing>()
}