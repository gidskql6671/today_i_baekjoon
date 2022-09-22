package com.example.today_i_baekjoon.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "commits")
public class Commit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private LocalDate commitDate;

	@Column(nullable = false, unique = true)
	private String commitUrl;
	
	@Column
	private String problemRank;
	
	@Column(nullable = false)
	private String problemTitle;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
}
