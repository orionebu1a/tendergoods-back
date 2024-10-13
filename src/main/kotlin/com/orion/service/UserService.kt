package com.orion.service;

import User
import UserRepository

class UserService(private val userRepository: UserRepository) {
    fun getAllUsers(): List<User> = userRepository.findAll()

    fun getUserById(id: Int): User? = userRepository.findById(id)

    fun registerUser(user: User): User = userRepository.create(user)

    fun findByLogin(login: String): User? = userRepository.findByLogin(login)

    fun updateUser(user: User): Boolean = userRepository.update(user)

    fun deleteUser(id: Int): Boolean = userRepository.delete(id)
}