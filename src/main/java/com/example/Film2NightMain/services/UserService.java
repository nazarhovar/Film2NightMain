package com.example.Film2NightMain.services;

import com.example.Film2NightMain.entities.User;

import java.time.Month;
import java.util.List;
import java.util.Map;


public interface UserService {
    User findUserById(Long userId);

    User findByUsername(String username);

    User saveUser(User user);

    List<User> getUsers();

    User getUserIdFromSecurityContext();

    Map<Integer, Long> countActiveUsersForYear(int year);

    Map<Month, Long> countActiveUsersForMonth(int year, int month);

    Map<String, Long> countActiveUsersForDay(int year, int month, int day);

    Map<String, Long> countActiveUsersForWeek(int year, int month, int week);
}
