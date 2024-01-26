package com.gidskql6671.today_i_baekjoon.domain

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Column(nullable = false)
    var username: String,

    val name: String = "",

    @OneToMany(mappedBy = "user")
    val commits: Set<Commit> = HashSet()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}