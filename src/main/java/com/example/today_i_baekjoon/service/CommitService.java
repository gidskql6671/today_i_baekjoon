package com.example.today_i_baekjoon.service;

import com.example.today_i_baekjoon.domain.Commit;
import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.dto.CommitInfo;
import com.example.today_i_baekjoon.dto.CommitWebhookRequest;
import com.example.today_i_baekjoon.repository.CommitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
                                .commitDate(commitRequest.getTimestamp())
                                .commitUrl(commitRequest.getUrl())
                                .build()
                )
                .toList();

        commitRepository.saveAll(commits);
    }
    
    public List<CommitInfo> getAllCommitsByDate(LocalDate date) {
        return commitRepository
                .findAllByCommitDate(date)
                .stream()
                .map(CommitInfo::fromEntity)
                .collect(Collectors.toList());
    }
    
    public List<CommitInfo> getAllCommitsBetweenDate(LocalDate start, LocalDate end) {
        return commitRepository
                .findAllByCommitDateBetween(start, end)
                .stream()
                .map(CommitInfo::fromEntity)
                .collect(Collectors.toList());
    }
}
