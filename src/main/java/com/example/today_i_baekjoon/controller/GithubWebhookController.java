package com.example.today_i_baekjoon.controller;

import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.dto.CommitWebhookRequest;
import com.example.today_i_baekjoon.service.CommitService;
import com.example.today_i_baekjoon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class GithubWebhookController {

    private final UserService userService;
    private final CommitService commitService;

    @PostMapping("/push")
    public void push(@Valid @RequestBody CommitWebhookRequest request) {
        System.out.println(request);
        String username = request.getPusherName();

        List<CommitWebhookRequest.Commit> commits = request.getCommits();
        User user = userService
                .findUserByUsername(username)
                .orElseGet(() -> userService.createUser(username));

        commitService.addCommits(user, commits);
    }
}
