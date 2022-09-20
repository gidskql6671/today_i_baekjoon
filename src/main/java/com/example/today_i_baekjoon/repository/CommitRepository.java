package com.example.today_i_baekjoon.repository;

import com.example.today_i_baekjoon.domain.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CommitRepository extends JpaRepository<Commit, Long> {

    boolean existsByCommitUrl(String commitUrl);

    List<Commit> findAllByCommitDateBetween(LocalDate start, LocalDate end);
    
    List<Commit> findAllByCommitDate(LocalDate date);
}
