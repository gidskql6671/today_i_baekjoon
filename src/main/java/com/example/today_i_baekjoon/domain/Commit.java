package com.example.today_i_baekjoon.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Commit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private LocalDateTime commitDateTime;
	
	
}
