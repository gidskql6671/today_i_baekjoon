package com.example.today_i_baekjoon.controller;

import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.dto.CommitInfo;
import com.example.today_i_baekjoon.exception.UserNotFoundException;
import com.example.today_i_baekjoon.service.CommitService;
import com.example.today_i_baekjoon.service.HolidayService;
import com.example.today_i_baekjoon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class HomeController {
	
	private final CommitService commitService;
	private final UserService userService;
	private final HolidayService holidayService;

	@GetMapping("/")
	public String homepage(Model model,
	                       @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Optional<LocalDate> date) {
		LocalDate today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate();
		LocalDate curDate = date.orElse(today);
		if (curDate.isAfter(today)) {
			return "redirect:/";
		}
		
		List<CommitInfo> commits = commitService.getAllCommitsByDate(curDate);
		List<User> users = userService.findAll();
		
		Map<String, List<CommitInfo>> userCommits = mappedCommitToUser(users, commits);
		Map<String, String> namesMappedUsername = new HashMap<>();
		users.forEach(user -> namesMappedUsername.put(user.getUsername(), user.getName()));
		
		boolean isHoliday = holidayService.isHoliday(curDate);

		model.addAttribute("namesMappedUsername", namesMappedUsername);
		model.addAttribute("userCommits", userCommits);
		model.addAttribute("isHoliday", isHoliday);
		model.addAttribute("curDate", curDate);
		// NOTE thymeleaf dates 라이브러리 사용이 안되는 버그가 있어서 여기서 다 넣어줌. 뭐가 문젠지 모르겠음;;
		model.addAttribute("prevDate", curDate.minusDays(1));
		if (curDate.isBefore(today)) {
			model.addAttribute("nextDate", curDate.plusDays(1));
		}

		return "index";
	}
	
	@GetMapping("/user/{username}")
	public String detail(Model model, 
	                     @PathVariable("username") String username,
	                     @DateTimeFormat(pattern = "yyyy-MM") @RequestParam("yearMonth") Optional<YearMonth> yearMonthReq) {
		YearMonth todayYearMonth = YearMonth.now(ZoneId.of("Asia/Seoul"));
		YearMonth yearMonth = yearMonthReq.orElse(todayYearMonth);
		if (yearMonth.isAfter(todayYearMonth)) {
			return "redirect:/user/" + username;
		}

		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		Optional<User> user = userService.findUserByUsername(username);
		if (user.isEmpty()) {
			throw new UserNotFoundException();
		}

		List<CommitInfo> commits = commitService.getAllCommitsBetweenDate(start, end, user.get());
		List<List<Integer>> calendar = getCalendarList(yearMonth);
		
		Map<Integer, List<CommitInfo>> dateCommits = mappedCommitToDate(commits);
		List<Integer> holidays = holidayService
				.getHolidaysBetweenDate(start, end)
				.stream()
				.map(LocalDate::getDayOfMonth)
				.toList();
		
		model.addAttribute("name", user.get().getName());
		
		model.addAttribute("dateCommits", dateCommits);
		model.addAttribute("calendar", calendar);
		model.addAttribute("holidays", holidays);

		model.addAttribute("yearMonth", yearMonth);
		model.addAttribute("prevYearMonth", yearMonth.minusMonths(1));
		if (yearMonth.isBefore(todayYearMonth)) {
			model.addAttribute("nextYearMonth", yearMonth.plusMonths(1));
		}
		
		return "detail";
	}

	private Map<String, List<CommitInfo>> mappedCommitToUser(List<User> users, List<CommitInfo> commitInfos) {
		Map<String, List<CommitInfo>> result = new HashMap<>();
		
		users.forEach(user -> result.put(user.getUsername(), new ArrayList<>()));
		commitInfos.forEach(commit -> result.get(commit.getUsername()).add(commit));
				
		return result;
	}
	
	private Map<Integer, List<CommitInfo>> mappedCommitToDate(List<CommitInfo> commitInfos) {
		Map<Integer, List<CommitInfo>> result = new HashMap<>();

		commitInfos
				.forEach(commit -> {
					int date = commit.getCommitDate().getDayOfMonth();

					result.computeIfAbsent(date, k -> new ArrayList<>()).add(commit);
				});

		return result;
	}
	
	private List<List<Integer>> getCalendarList(YearMonth yearMonth) {
		List<List<Integer>> calendar = new ArrayList<>();
		
		LocalDate start = yearMonth.atDay(1);
		int endDate = yearMonth.atEndOfMonth().getDayOfMonth();

		int startDateOfCalendar = 1 - (start.getDayOfWeek().getValue() % 7);
		
		int dateCount = endDate - startDateOfCalendar;
		dateCount = ((dateCount - 1) / 7 + 1) * 7;

		
		for(int i = 0; i < dateCount; i += 7) {
			List<Integer> week = new ArrayList<>();
			for(int j = i; j < i + 7; j++) {
				int curDate = startDateOfCalendar + j;
				
				if (0 < curDate && curDate <= endDate) {
					week.add(curDate);
				}
				else {
					week.add(null);
				}
			}
			calendar.add(week);
		}
		
		return calendar;
	}
}
