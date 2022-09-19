package com.example.today_i_baekjoon.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "commits")
public class Commit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private LocalDateTime commitDateTime;

	@Column(nullable = false, unique = true)
	private String commitUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public Commit(LocalDateTime commitDateTime, String commitUrl, User user) {
		this.commitDateTime = commitDateTime;
		this.commitUrl = commitUrl;
		this.user = user;
	}
}
