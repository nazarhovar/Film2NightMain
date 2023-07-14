package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.UserRepository;
import com.example.Film2NightMain.services.UserService;
import org.springframework.stereotype.Service;
import lombok.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
