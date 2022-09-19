package com.example.today_i_baekjoon.service;

import com.example.today_i_baekjoon.domain.Commit;
import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.dto.CommitWebhookRequest;
import com.example.today_i_baekjoon.repository.CommitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommitService {

    private final CommitRepository commitRepository;

    public void addCommits(User user, List<CommitWebhookRequest.Commit> commitRequests) {
        List<Commit> commits = commitRequests
                .stream()
                .filter(commitRequest -> !commitRepository.existsByCommitUrl(commitRequest.getUrl()))
                .map(commitRequest ->
                        Commit.builder()
                                .user(user)
                                .commitDateTime(commitRequest.getTimestamp())
                                .commitUrl(commitRequest.getUrl())
                                .build()
                )
                .toList();

        commitRepository.saveAll(commits);
    }
}
