package com.example.today_i_baekjoon.repository;

import com.example.today_i_baekjoon.domain.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitRepository extends JpaRepository<Commit, Long> {

    boolean existsByCommitUrl(String commitUrl);
}
