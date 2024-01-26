package com.gidskql6671.today_i_baekjoon.controller

import com.gidskql6671.today_i_baekjoon.dto.CommitWebhookRequest
import com.gidskql6671.today_i_baekjoon.exception.UserNotFoundException
import com.gidskql6671.today_i_baekjoon.service.CommitService
import com.gidskql6671.today_i_baekjoon.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/webhook")
class GithubWebhookController(
    private val userService: UserService,
    private val commitService: CommitService
) {

    @PostMapping("/push")
    fun push(
        @RequestHeader(value = "X-GitHub-Event") githubEvent: String,
        @RequestBody request: CommitWebhookRequest
    ): ResponseEntity<String> {
        if (githubEvent == "ping") {
            return ResponseEntity.ok().body("valid webhook")
        } else if (githubEvent != "push") {
            return ResponseEntity.badRequest().body("Only allowed if 'X-Github-Event' is 'push'.")
        }

        val username: String = request.pusher.name

        val commits: List<CommitWebhookRequest.Commit> = request.commits
        val user = userService.findUserByUsername(username) ?: throw UserNotFoundException()

        commitService.addCommits(user, commits)

        return ResponseEntity.ok().build()
    }

}