package com.example.today_i_baekjoon.service;

import com.example.today_i_baekjoon.domain.Commit;
import com.example.today_i_baekjoon.domain.Holiday;
import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.repository.CommitRepository;
import com.example.today_i_baekjoon.repository.HolidayRepository;
import com.example.today_i_baekjoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FineService {
    private final CommitRepository commitRepository;
    private final HolidayRepository holidayRepository;
    private final UserRepository userRepository;

    private static final int FINE_PER_DATE = 500;

    public Map<User, Integer> getFineBetweenDate(LocalDate start, LocalDate end) {
        List<User> users = userRepository.findAll();
        List<Commit> commits = commitRepository.findAllByCommitDateBetween(start, end);
        List<LocalDate> holidays = holidayRepository
                .findAllByDateBetween(start, end)
                .stream().map(Holiday::getDate).toList();

        Map<User, Integer> result = new HashMap<>();
        users.forEach(user -> result.put(user, 0));

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (holidays.contains(date)) {
                continue;
            }

            LocalDate finalDate = date;
            List<Commit> commitsOfDate = commits
                    .stream()
                    .filter(commit -> commit.getCommitDate().isEqual(finalDate))
                    .toList();

            for (User user : users) {
                boolean existsCommit = false;
                for (Commit commit : commitsOfDate) {
                    if (commit.getUser() == user) {
                        existsCommit = true;
                        break;
                    }
                }
                if (!existsCommit) {
                    result.compute(user, (k, v) -> v + FINE_PER_DATE);
                }
            }
        }

        return result;
    }
}
