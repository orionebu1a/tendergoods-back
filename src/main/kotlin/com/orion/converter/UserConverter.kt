package com.orion.converter

import User
import com.orion.model.UserDto

fun UserDto.toModel(): User {
    return User.new {
        email = this@toModel.email
        passwordHash = this@toModel.passwordHash
        firstName = this@toModel.firstName
        lastName = this@toModel.lastName
        age = this@toModel.age
        gender = this@toModel.gender
        rating = this@toModel.rating?.toBigDecimal()
        walletBalance = this@toModel.walletBalance.toBigDecimal()
        createdAt = this@toModel.createdAt!!
        updatedAt = this@toModel.updatedAt!!
    }
}

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id.value,
        email = this.email,
        passwordHash = this.passwordHash,
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