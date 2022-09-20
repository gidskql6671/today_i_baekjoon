package com.example.today_i_baekjoon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommitWebhookRequest {
    @Getter
    private List<Commit> commits = new ArrayList<>();

    @NotNull
    @JsonProperty("pusher")
    private Pusher pusher;

    public String getPusherName() {
        return pusher.getName();
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
        
        public LocalDate getTimestamp() {
            return timestamp.toLocalDate();
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

