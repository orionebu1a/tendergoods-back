package com.orion.service;

import User
import com.orion.form.UserDto
import com.orion.repository.UserRepository
import io.ktor.server.auth.*

class UserService(private val userRepository: UserRepository) {
    fun getAllUsers(): List<UserDto> = userRepository.findAll()

    fun getUserById(id: Int): UserDto? = userRepository.findById(id)

    fun getPrincipalById(id: Int): User? = userRepository.findPrincipalById(id)

    fun registerUser(user: UserDto): UserDto = userRepository.create(user)

    fun findByLogin(login: String): UserDto? = userRepository.findByLogin(login)

    fun findPrincipalByLogin(login: String): User? = userRepository.findPrincipalByLogin(login)

    fun updateUser(id: Int, user: UserDto): Boolean = userRepository.update(id, user)

    fun deleteUser(id: Int): Boolean = userRepository.delete(id)
}