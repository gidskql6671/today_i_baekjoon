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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommitService {

    private final CommitRepository commitRepository;
    private final Pattern pattern = Pattern.compile("^\\[(?<rank>.*)] Title: (?<title>.*), Time:.*");

    public void addCommits(User user, List<CommitWebhookRequest.Commit> commitRequests) {
        List<Commit> commits = commitRequests
                .stream()
                .filter(commitRequest -> !commitRepository.existsByCommitUrl(commitRequest.getUrl()))
                .map(commitRequest -> {
                    Matcher matcher = pattern.matcher(commitRequest.getMessage());
                    String rank = "", title = "";
                    if (matcher.matches()) {
                        rank = matcher.group("rank");
                        title = matcher.group("title");
                    }
                    
                    return Commit.builder()
                            .user(user)
                            .commitDate(commitRequest.getTimestamp())
                            .commitUrl(commitRequest.getUrl())
                            .problemTitle(title)
                            .problemRank(rank)
                            .build();
                })
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
    
    public List<CommitInfo> getAllCommitsBetweenDate(LocalDate start, LocalDate end, User user) {
        return commitRepository
                .findAllByCommitDateBetweenAndUser(start, end, user)
                .stream()
                .map(CommitInfo::fromEntity)
                .collect(Collectors.toList());
    }
}
