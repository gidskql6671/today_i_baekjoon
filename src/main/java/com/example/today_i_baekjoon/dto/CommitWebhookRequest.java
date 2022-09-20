package com.example.today_i_baekjoon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
public class CommitWebhookRequest {
    private ArrayList<Commit> commits;

    @NotNull
    @JsonProperty("head_commit")
    private Commit headCommit;

    @NotNull
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
        @NotBlank
        private String url;
        @NotNull
        private LocalDateTime timestamp;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pusher {
        @NotBlank
        private String name;
        @NotBlank
        private String email;
    }

}

