package com.orion.service

import User
import UserTable
import com.orion.converter.toDto
import com.orion.model.UserDto
import com.orion.model.UserForm
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class UserService {
    fun findAll(): List<UserDto> = transaction {
        val user = User.all().toList()
        return@transaction user.map { it.toDto() }
    }

    fun findById(id: Int): UserDto? = transaction {
        val user = User.findById(id)
        return@transaction user?.toDto()
    }

    fun findPrincipalById(id: Int): User? = transaction {
        User.findById(id)
    }

    fun findByLogin(login: String): UserDto? = transaction {
        val user = User.find { UserTable.email eq login }.toList().firstOrNull()
        return@transaction user?.toDto()
    }

    fun findPrincipalByLogin(login: String): User? = transaction {
        User.find { UserTable.email eq login }.toList().firstOrNull()
    }

    fun create(userDto: UserForm): UserDto = transaction {
        val user = User.new {
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
        return@transaction user.toDto()
    }

    fun update(id: Int, userDto: UserForm): Boolean = transaction {
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