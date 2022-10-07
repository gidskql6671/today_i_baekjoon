package com.example.today_i_baekjoon.repository;

import com.example.today_i_baekjoon.domain.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    Optional<Holiday> findByDate(LocalDate date);
    boolean existsByDate(LocalDate date);

    void deleteByDateIn(List<LocalDate> date);
}
