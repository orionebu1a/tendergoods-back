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

    fun findPrincipalByLogin(login: String): User? = transaction {
        User.find { UserTable.email eq login }.firstOrNull()
    }

    fun create(userForm: UserForm, hashedPassword: String): ResultWithError<UserDto> = transaction {
        try {
            val user = User.new {
                email = userForm.email
                passwordHash = hashedPassword
                firstName = userForm.firstName
                lastName = userForm.lastName
                age = userForm.age
                gender = userForm.gender
                rating = userForm.rating
                walletBalance = userForm.walletBalance
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
            user.passwordHash = userDto.password
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