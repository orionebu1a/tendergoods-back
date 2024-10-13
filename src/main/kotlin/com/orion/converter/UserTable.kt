package com.orion.converter

import User
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.sql.Timestamp

fun ResultRow.toUser(): User {
    return User(
        id = this[UserTable.id].value,
        email = this[UserTable.email],
        passwordHash = this[UserTable.passwordHash],
        firstName = this[UserTable.firstName],
        lastName = this[UserTable.lastName],
        age = this[UserTable.age],
        gender = this[UserTable.gender],
        rating = this[UserTable.rating].toDouble(),
        walletBalance = this[UserTable.walletBalance].toDouble(),
        createdAt = this[UserTable.createdAt],
        updatedAt = this[UserTable.updatedAt]
    )
}

fun InsertStatement<EntityID<Int>>.fromUser(user: User) {
    this[UserTable.email] = user.email
    this[UserTable.passwordHash] = user.passwordHash
    user.firstName?.let { this[UserTable.firstName] = it }
    user.lastName?.let { this[UserTable.lastName] = it }
    user.age?.let { this[UserTable.age] = it }
    user.gender?.let { this[UserTable.gender] = it }
    user.rating?.let { this[UserTable.rating] = it.toBigDecimal() }
    this[UserTable.walletBalance] = user.walletBalance.toBigDecimal()
    this[UserTable.createdAt] = user.createdAt
    this[UserTable.updatedAt] = user.updatedAt
}

fun UpdateStatement.fromUser(user: User) {
    this[UserTable.email] = user.email
    this[UserTable.passwordHash] = user.passwordHash
    user.firstName?.let { this[UserTable.firstName] = it }
    user.lastName?.let { this[UserTable.lastName] = it }
    user.age?.let { this[UserTable.age] = it }
    user.gender?.let { this[UserTable.gender] = it }
    user.rating?.let { this[UserTable.rating] = it.toBigDecimal() }
    this[UserTable.walletBalance] = user.walletBalance.toBigDecimal()
    this[UserTable.createdAt] = user.createdAt
    this[UserTable.updatedAt] = user.updatedAt
}