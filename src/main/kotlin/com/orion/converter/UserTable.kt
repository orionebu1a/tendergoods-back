package com.orion.converter

import User
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.sql.Timestamp

fun ResultRow.toUser(): User {
    return User(
        id = this[Users.id].value,
        email = this[Users.email],
        passwordHash = this[Users.passwordHash],
        firstName = this[Users.firstName],
        lastName = this[Users.lastName],
        age = this[Users.age],
        gender = this[Users.gender],
        rating = this[Users.rating].toDouble(),
        walletBalance = this[Users.walletBalance].toDouble(),
        createdAt = (this[Users.createdAt] as Timestamp).toInstant(),
        updatedAt = (this[Users.updatedAt] as Timestamp).toInstant()
    )
}



fun InsertStatement<EntityID<Int>>.fromUser(user: User) {
    this[Users.email] = user.email
    this[Users.passwordHash] = user.passwordHash
    this[Users.firstName] = user.firstName
    this[Users.lastName] = user.lastName
    this[Users.age] = user.age
    this[Users.gender] = user.gender
    this[Users.rating] = user.rating
    this[Users.walletBalance] = user.walletBalance
    this[Users.createdAt] = user.createdAt
    this[Users.updatedAt] = user.updatedAt
}

fun UpdateStatement.fromUser(user: User) {
    this[Users.email] = user.email
    this[Users.passwordHash] = user.passwordHash
    this[Users.firstName] = user.firstName
    this[Users.lastName] = user.lastName
    this[Users.age] = user.age
    this[Users.gender] = user.gender
    this[Users.rating] = user.rating
    this[Users.walletBalance] = user.walletBalance
    this[Users.createdAt] = user.createdAt
    this[Users.updatedAt] = user.updatedAt
}