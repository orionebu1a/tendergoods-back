package com.orion.service

import com.orion.entity.User
import com.orion.converter.toDto
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import com.orion.model.UserDto
import com.orion.model.UserForm
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class UserService {
    fun findAll(): ResultWithError<List<UserDto>> = transaction {
        try {
            val users = User.all().toList()
            ResultWithError.Success(users.map { it.toDto() })
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Database error"))
        }
    }

    fun findById(id: Int): ResultWithError<UserDto> = transaction {
        val user = User.findById(id)
        if (user != null) {
            ResultWithError.Success(user.toDto())
        } else {
            ResultWithError.Failure(ServiceError.NotFound)
        }
    }

    fun findPrincipalById(id: Int): User? = transaction {
        User.findById(id)
    }

    fun findByLogin(login: String): ResultWithError<UserDto> = transaction {
        val user = User.find { UserTable.email eq login }.firstOrNull()
        if (user != null) {
            ResultWithError.Success(user.toDto())
        } else {
            ResultWithError.Failure(ServiceError.NotFound)
        }
    }

    fun findPrincipalByLogin(login: String): User? = transaction {
        User.find { UserTable.email eq login }.firstOrNull()
    }

    fun create(userDto: UserForm): ResultWithError<UserDto> = transaction {
        try {
            val user = User.new {
                email = userDto.email
                passwordHash = userDto.passwordHash
                firstName = userDto.firstName
                lastName = userDto.lastName
                age = userDto.age
                gender = userDto.gender
                rating = userDto.rating
                walletBalance = userDto.walletBalance
                createdAt = Instant.now()
                updatedAt = Instant.now()
            }
            ResultWithError.Success(user.toDto())
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Creation failed"))
        }
    }

    fun update(id: Int, userDto: UserForm): ResultWithError<Boolean> = transaction {
        val user = User.findById(id) ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)

        try {
            user.email = userDto.email
            user.passwordHash = userDto.passwordHash
            user.firstName = userDto.firstName
            user.lastName = userDto.lastName
            user.age = userDto.age
            user.gender = userDto.gender
            user.rating = userDto.rating
            user.walletBalance = userDto.walletBalance
            user.updatedAt = Instant.now()
            ResultWithError.Success(true)
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Update failed"))
        }
    }

    fun delete(id: Int): ResultWithError<Boolean> = transaction {
        val user = User.findById(id) ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)

        try {
            user.delete()
            ResultWithError.Success(true)
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Delete failed"))
        }
    }
}