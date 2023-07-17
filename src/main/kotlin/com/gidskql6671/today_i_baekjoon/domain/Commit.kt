package com.gidskql6671.today_i_baekjoon.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "commits")
class Commit(
    @Column(nullable = false)
    var commitDate: LocalDate,

    @Column(nullable = false, unique = true)
    var commitUrl: String,

    var problemRank: String,

    @Column(nullable = false)
    var problemTitle: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}