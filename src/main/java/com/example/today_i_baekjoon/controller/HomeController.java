package com.example.today_i_baekjoon.controller;

import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.dto.CommitInfo;
import com.example.today_i_baekjoon.service.CommitService;
import com.example.today_i_baekjoon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
	
	private final CommitService commitService;
	private final UserService userService;

	@GetMapping("/")
	public String homepage(Model model,
	                       @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Optional<LocalDate> date) {
		LocalDate today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate();
		LocalDate curDate = date.orElse(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate());
		if (curDate.isAfter(today)) {
			return "redirect:/";
		}
		
		List<CommitInfo> commits = commitService.getAllCommitsByDate(curDate);
		List<User> users = userService.findAll();
		
		Map<String, List<CommitInfo>> userCommits = mappedCommitToUser(users, commits);

		log.info(String.valueOf(userCommits));
		model.addAttribute("userCommits", userCommits);
		model.addAttribute("curDate", curDate);
		// NOTE thymeleaf dates 라이브러리 사용이 안되는 버그가 있어서 여기서 다 넣어줌. 뭐가 문젠지 모르겠음;;
		model.addAttribute("prevDate", curDate.minusDays(1));
		if (curDate.isBefore(today)) {
			model.addAttribute("nextDate", curDate.plusDays(1));
		}

		return "index";
	}
	
	
	private Map<String, List<CommitInfo>> mappedCommitToUser(List<User> users, List<CommitInfo> commitInfos) {
		Map<String, List<CommitInfo>> result = new HashMap<>();
		
		users.forEach(user -> result.put(user.getUsername(), new ArrayList<>()));
		commitInfos.forEach(commit -> result.get(commit.getUsername()).add(commit));
				
		return result;
	}
}
