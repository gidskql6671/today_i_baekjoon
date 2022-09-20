package com.example.today_i_baekjoon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommitWebhookRequest {
    private List<Commit> commits = new ArrayList<>();

    @NotNull
    @JsonProperty("head_commit")
    private Commit headCommit;

    @NotNull
    @JsonProperty("pusher")
    private Pusher pusher;

    public String getPusherName() {
        return pusher.getName();
    }

    public List<Commit> getNewCommits() {
        List<Commit> result = new ArrayList<>(commits);
        result.add(headCommit);

        return result;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Commit {
        @Getter
        @NotBlank
        private String url;
        
        @NotNull
        private ZonedDateTime timestamp;
        
        public LocalDateTime getTimestamp() {
            return timestamp.toLocalDateTime();
        }
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

