package com.example.Film2NightMain.services;

import com.example.Film2NightMain.entities.User;


public interface UserService {
    User findUserById(Long userId);

    User findByUsername(String username);

    User saveUser(User user);
}
