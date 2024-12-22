package com.orion.converter

import com.orion.entity.User
import com.orion.model.UserDto

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id.value,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        age = this.age,
        gender = this.gender,
        rating = this.rating?.toDouble(),
        walletBalance = this.walletBalance.toDouble(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}