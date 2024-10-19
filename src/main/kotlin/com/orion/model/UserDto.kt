package com.orion.model

import java.time.Instant

data class UserDto (
    val id: Int,
    val email: String,
    val passwordHash: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val rating: Double? = null,
    val walletBalance: Double,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

data class UserForm (
    val email: String,
    val passwordHash: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val rating: Double? = null,
    val walletBalance: Double,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)