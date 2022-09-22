package com.example.today_i_baekjoon.controller;

import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.dto.CommitWebhookRequest;
import com.example.today_i_baekjoon.service.CommitService;
import com.example.today_i_baekjoon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class GithubWebhookController {

    private final UserService userService;
    private final CommitService commitService;

    @PostMapping("/push")
    public ResponseEntity<String> push(
            @RequestHeader(value = "X-GitHub-Event") String githubEvent,
            @Valid @RequestBody CommitWebhookRequest request) {
        if (!Objects.equals(githubEvent, "push")) {
            return ResponseEntity.badRequest().body("Only allowed if 'X-Github-Event' is 'push'.");
        }
        
        String username = request.getPusherName();

        List<CommitWebhookRequest.Commit> commits = request.getCommits();
        User user = userService
                .findUserByUsername(username)
                .orElseGet(() -> userService.createUser(username));

        commitService.addCommits(user, commits);
        
        return ResponseEntity.ok().build();
    }
}
