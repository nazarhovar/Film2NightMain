package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.UserRepository;
import com.example.Film2NightMain.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        log.info("Get user {} ", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public User findByUsernameRegister(String username) {
        log.info("Get user {} ", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }

    public Map<Integer, Long> countActiveUsersForYear(int year) {
        LocalDateTime dateBegin = Year.of(year).atMonth(1).atDay(1).atTime(0, 0, 0);
        LocalDateTime dateEnd = Year.of(year).atMonth(12).atEndOfMonth().atTime(23, 59, 59);

        return Collections.singletonMap(year, countActiveUsersForDateRange(dateBegin, dateEnd));
    }

    public Map<Month, Long> countActiveUsersForMonth(int year, int month) {
        Month monthReport = Month.of(month);
        LocalDateTime dateBegin = Year.of(year).atMonth(monthReport).atDay(1).atTime(0, 0, 0);
        LocalDateTime dateEnd = Year.of(year).atMonth(monthReport).atEndOfMonth().atTime(23, 59, 59);

        return Collections.singletonMap(monthReport, countActiveUsersForDateRange(dateBegin, dateEnd));
    }

    public Map<String, Long> countActiveUsersForWeek(int year, int month, int week) {
        Month monthReport = Month.of(month);
        LocalDateTime dateBegin = Year.of(year).atMonth(monthReport).atDay(1)
                .atTime(0, 0, 0).plusWeeks(week - 1);
        LocalDateTime dateEnd = Year.of(year).atMonth(monthReport).atDay(1)
                .atTime(0, 0, 0).plusWeeks(week);

        return Collections.singletonMap(dateBegin + " - " + dateEnd, countActiveUsersForDateRange(dateBegin, dateEnd));
    }

    public Map<String, Long> countActiveUsersForDay(int year, int month, int day) {
        Month monthReport = Month.of(month);
        LocalDateTime dateBegin = Year.of(year).atMonth(monthReport).atDay(day).atTime(0, 0, 0);
        LocalDateTime dateEnd = Year.of(year).atMonth(monthReport).atDay(day).atTime(23, 59, 59);

        return Collections.singletonMap(dateBegin + " - " + dateEnd, countActiveUsersForDateRange(dateBegin, dateEnd));
    }

    private Long countActiveUsersForDateRange(LocalDateTime dateBegin, LocalDateTime dateEnd) {
        List<User> activeUsers = userRepository.findActiveUsers(dateBegin, dateEnd);
        return (long) activeUsers.size();
    }


}
