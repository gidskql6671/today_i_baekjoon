package com.gidskql6671.today_i_baekjoon.service

import com.gidskql6671.today_i_baekjoon.domain.Commit
import com.gidskql6671.today_i_baekjoon.domain.User
import com.gidskql6671.today_i_baekjoon.dto.CommitWebhookRequest
import com.gidskql6671.today_i_baekjoon.repository.CommitRepository
import com.gidskql6671.today_i_baekjoon.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.time.ZonedDateTime

internal class CommitServiceTest : BehaviorSpec() {
    val commitRepository: CommitRepository = mockk()
    val userRepository: UserRepository = mockk()
    val commitService = CommitService(commitRepository, userRepository)


    init {
        Given("추가할 커밋들이 존재하는데") {
            val user = mockk<User>()
            val commitRequests: List<CommitWebhookRequest.Commit> = listOf(
                CommitWebhookRequest.Commit("url1", "[Silver 1] Title: title1, Time: ", ZonedDateTime.now()),
                CommitWebhookRequest.Commit("url2", "[Silver 2] Title: title2, Time: ", ZonedDateTime.now()),
                CommitWebhookRequest.Commit("url3", "[Silver 3] Title: title3, Time: ", ZonedDateTime.now())
            )

            When("모든 커밋들이 중복된 url을 가지지 않는다면") {
                every { commitRepository.existsByCommitUrl(any()) } returns false

                val slot = slot<List<Commit>>()
                every { commitRepository.saveAll(capture(slot)) } returns emptyList()

                commitService.addCommits(user, commitRequests)

                Then("모든 커밋들은 저장되어야 한다.") {
                    verify {
                        commitRepository.saveAll<Commit>(any())
                    }

                    val arg = slot.captured
                    println(arg.size)
                    assert(arg.size == commitRequests.size)
                }
            }

            When("중복된 url에 대한 커밋이 존재한다면") {
//                val urlSlot = slot<String>()
//                every {
//                    commitRepository.existsByCommitUrl(capture(urlSlot))
//                } answers {
//                    urlSlot.captured == "url1"
//                }

                every { commitRepository.existsByCommitUrl(any()) } returnsMany listOf(true, false)

                val saveSlot = slot<List<Commit>>()
                every { commitRepository.saveAll(capture(saveSlot)) } returns emptyList()

                commitService.addCommits(user, commitRequests)

                Then("중복된 커밋들은 저장되지 않는다.") {
                    verify {
                        commitRepository.saveAll<Commit>(any())
                    }

                    val arg = saveSlot.captured
                    println(arg.size)
                    assert(arg.size == commitRequests.size - 1)
                }
            }
        }
    }

}