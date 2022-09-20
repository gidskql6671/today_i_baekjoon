package com.example.today_i_baekjoon.dto;

import com.example.today_i_baekjoon.domain.Commit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@AllArgsConstructor
@Builder
@Getter
public class CommitInfo {

	private LocalDate commitDate;
	private String commitUrl;
	private String username;

	public static CommitInfo fromEntity(Commit commit) {
		return CommitInfo.builder()
				.commitDate(commit.getCommitDate())
				.commitUrl(commit.getCommitUrl())
				.username(commit.getUser().getUsername())
				.build();
	}
}
