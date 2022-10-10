package com.example.today_i_baekjoon.controller;

import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.dto.ShowFineOfWeekModel;
import com.example.today_i_baekjoon.service.CommitService;
import com.example.today_i_baekjoon.service.FineService;
import com.example.today_i_baekjoon.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Controller
@RequestMapping("/fine")
@RequiredArgsConstructor
public class FineController {

	private final CommitService commitService;
	private final FineService fineService;
	private final HolidayService holidayService;
	
	@GetMapping("/month")
	public String showFineOfMonth(Model model,
	                              @DateTimeFormat(pattern = "yyyy-MM") @RequestParam("yearMonth") Optional<YearMonth> yearMonthReq) {
		YearMonth todayYearMonth = YearMonth.now(ZoneId.of("Asia/Seoul"));
		YearMonth yearMonth = yearMonthReq.orElse(todayYearMonth);
		if (yearMonth.isAfter(todayYearMonth)) {
			return "redirect:/find/month";
		}

		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		LocalDate today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate();
		if (end.isAfter(today)) {
			end = today;
		}

		Map<String, Integer> fines = getFinesBetweenDate(start, end);

		model.addAttribute("fines", fines);
		model.addAttribute("yearMonth", yearMonth);
		model.addAttribute("prevYearMonth", yearMonth.minusMonths(1));
		if (yearMonth.isBefore(todayYearMonth)) {
			model.addAttribute("nextYearMonth", yearMonth.plusMonths(1));
		}

		return "fine_of_month";
	}

	@GetMapping("/week")
	public String showFineOfWeek(Model model,
	                             @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("startDate") Optional<LocalDate> startDate) {
		LocalDate today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate();
		LocalDate curDate = startDate.orElse(today);
		if (curDate.isAfter(today)) {
			return "redirect:/";
		}

		// NOTE 월요일 1, 일요일 7... Enum을 int로 쓴다는게 좋은 방법은 아니겠지만...
		int dayOfWeek = curDate.getDayOfWeek().getValue();
		LocalDate start = curDate.minusDays((dayOfWeek - 1));
		LocalDate end = start.plusDays(4);
		
		List<LocalDate> holidays = holidayService.getHolidaysBetweenDate(start, end);
		
		List<Boolean> isHolidays = new ArrayList<>();
		List<LocalDate> dates = new ArrayList<>();
		for(LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
			dates.add(date);
			isHolidays.add(holidays.contains(date));
		}
		
		Map<String, Integer> fines = getFinesBetweenDate(start, end);

		Map<User, List<Boolean>> solved = commitService.getSolvedListBetweenDate(start, end);
		Map<String, List<Boolean>> solvedList = convertKeyUserToUsername(solved);

		ShowFineOfWeekModel data = ShowFineOfWeekModel.builder()
				.dates(dates)
				.prevStartDate(start.minusDays(7))
				.nextStartDate(today.isBefore(start.plusDays(7)) ? null : start.plusDays(7))
				.fines(fines)
				.solvedList(solvedList)
				.isHolidays(isHolidays)
				.build();
		
		model.addAttribute("data", data);
		
		return "fine_of_week";
	}

	private Map<String, Integer> getFinesBetweenDate(LocalDate start, LocalDate end) {
		Map<User, Integer> finesMappedUser = fineService.getFineBetweenDate(start, end);
		
		Map<String, Integer> result = new HashMap<>();
		finesMappedUser.forEach((k, v) -> result.put(k.getName(), v));
		
		return result;
	}
	
	private Map<String, List<Boolean>> convertKeyUserToUsername(Map<User, List<Boolean>> source) {
		Map<String, List<Boolean>> result = new HashMap<>();
		source.forEach((k, v) -> result.put(k.getName(), v));

		return result;
	}
}
