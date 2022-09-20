package com.example.today_i_baekjoon.controller;

import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.dto.CommitInfo;
import com.example.today_i_baekjoon.service.CommitService;
import com.example.today_i_baekjoon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
	
	private final CommitService commitService;
	private final UserService userService;

	@GetMapping("/")
	public String homepage(Model model) {
		List<CommitInfo> commits = 
				commitService.getAllCommitsByDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate());
		List<User> users = userService.findAll();
		
		Map<String, List<CommitInfo>> userCommits = mappedCommitToUser(users, commits);

		log.debug(String.valueOf(userCommits));
		model.addAttribute("userCommits", userCommits);

		return "index";
	}
	
	
	private Map<String, List<CommitInfo>> mappedCommitToUser(List<User> users, List<CommitInfo> commitInfos) {
		Map<String, List<CommitInfo>> result = new HashMap<>();
		
		users.forEach(user -> result.put(user.getUsername(), new ArrayList<>()));
		commitInfos.forEach(commit -> result.get(commit.getUsername()).add(commit));
				
		return result;
	}
}
