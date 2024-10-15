package com.orion.service;

import User
import com.orion.converter.toDto
import com.orion.form.UserDto
import com.orion.repository.UserRepository

class UserService(private val userRepository: UserRepository) {
    fun getAllUsers(): List<User> = userRepository.findAll()

    fun getUserById(id: Int): User? = userRepository.findById(id)

    fun registerUser(user: UserDto): User = userRepository.create(user)

    fun findByLogin(login: String): User = userRepository.findByLogin(login)

    fun updateUser(id: Int, user: UserDto): Boolean = userRepository.update(id, user)

    fun deleteUser(id: Int): Boolean = userRepository.delete(id)
}