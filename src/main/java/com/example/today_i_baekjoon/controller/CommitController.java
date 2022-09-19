package com.example.today_i_baekjoon.controller;

import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.dto.CommitWebhookRequest;
import com.example.today_i_baekjoon.service.CommitService;
import com.example.today_i_baekjoon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommitController {

    private final UserService userService;
    private final CommitService commitService;

    @PostMapping("/")
    public void createCommit(@RequestBody CommitWebhookRequest request) {
        String username = request.getPusherName();

        List<CommitWebhookRequest.Commit> commits = request.getCommits();
        User user = userService
                .findUserByUsername(username)
                .orElseGet(() -> userService.createUser(username));;

        commitService.addCommits(user, commits);
    }
}
