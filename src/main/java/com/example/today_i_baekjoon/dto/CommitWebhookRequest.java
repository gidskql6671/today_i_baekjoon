package com.example.today_i_baekjoon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
public class CommitWebhookRequest {
    private ArrayList<Commit> commits;

    @JsonProperty("head_commit")
    private Commit headCommit;

    @JsonProperty("pusher")
    private Pusher pusher;

    public String getPusherName() {
        return pusher.getName();
    }

    public List<Commit> getCommits() {
        List<Commit> result = new ArrayList<>(commits);
        result.add(headCommit);

        return result;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Commit {
        private String url;
        private LocalDateTime timestamp;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pusher {
        private String name;
        private String email;
    }

}

