package com.gidskql6671.today_i_baekjoon.service

import com.gidskql6671.today_i_baekjoon.domain.User
import com.gidskql6671.today_i_baekjoon.repository.UserRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun findUser(userId: Long): User? {
        return userRepository.findById(userId).getOrNull()
    }

    fun findUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun createUser(username: String): User {
        return userRepository.save(User(username = username))
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }
}