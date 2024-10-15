package com.orion.repository

import User
import UserTable
import com.orion.form.UserDto
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class UserRepository {

    fun findAll(): List<User> = transaction {
        User.all().toList()
    }

    fun findById(id: Int): User? = transaction {
        User.findById(id)
    }

    fun findByLogin(login: String): User = transaction {
        User.find { UserTable.email eq login }.toList().first()
    }

    fun create(userDto: UserDto): User = transaction {
        User.new {
            email = userDto.email
            passwordHash = userDto.passwordHash
            firstName = userDto.firstName
            lastName = userDto.lastName
            age = userDto.age
            gender = userDto.gender
            rating = userDto.rating?.toBigDecimal()
            walletBalance = userDto.walletBalance.toBigDecimal()
            createdAt = Instant.now()
            updatedAt = Instant.now()
        }
    }

    fun update(id: Int, userDto: UserDto): Boolean = transaction {
        val user = User.findById(id) ?: return@transaction false

        user.email = userDto.email
        user.passwordHash = userDto.passwordHash
        user.firstName = userDto.firstName
        user.lastName = userDto.lastName
        user.age = userDto.age
        user.gender = userDto.gender
        user.rating = userDto.rating?.toBigDecimal()
        user.walletBalance = userDto.walletBalance.toBigDecimal()
        user.updatedAt = Instant.now()

        return@transaction true
    }

    fun delete(id: Int): Boolean = transaction {
        val user = User.findById(id)
        if (user == null) {
            return@transaction false
        } else {
            user.delete()
            return@transaction true
        }
    }
}