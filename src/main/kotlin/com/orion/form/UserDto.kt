package com.orion.form

import java.time.Instant

data class UserDto (
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