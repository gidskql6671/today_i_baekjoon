package com.example.today_i_baekjoon.repository;

import com.example.today_i_baekjoon.domain.Commit;
import com.example.today_i_baekjoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CommitRepository extends JpaRepository<Commit, Long> {

    boolean existsByCommitUrl(String commitUrl);

    List<Commit> findAllByCommitDateBetween(LocalDate start, LocalDate end);
    List<Commit> findAllByCommitDateBetweenAndUser(LocalDate start, LocalDate end, User user);
    
    List<Commit> findAllByCommitDate(LocalDate date);
}
