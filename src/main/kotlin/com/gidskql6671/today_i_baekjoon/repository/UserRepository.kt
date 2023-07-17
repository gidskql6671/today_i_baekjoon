package com.gidskql6671.today_i_baekjoon.repository

import com.gidskql6671.today_i_baekjoon.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {

    fun findByUsername(username: String): User?
}