package com.example.today_i_baekjoon.service;

import com.example.today_i_baekjoon.domain.User;
import com.example.today_i_baekjoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(String username) {
        return userRepository.save(new User(username));
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
}

