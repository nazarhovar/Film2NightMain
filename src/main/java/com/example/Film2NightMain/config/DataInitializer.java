package com.example.Film2NightMain.config;

import com.example.Film2NightMain.entities.*;
import com.example.Film2NightMain.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataInitializer.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            log.info("Data already initialized");
            return;
        }

        log.info("Initializing roles and users...");

        Role adminRole = roleRepository.save(createRole("ADMIN"));
        Role moderatorRole = roleRepository.save(createRole("MODERATOR"));
        Role userRole = roleRepository.save(createRole("USER"));

        userRepository.save(createUser("admin", "Admin", "Adminov", "admin123", false, List.of(adminRole)));
        userRepository.save(createUser("moderator", "Moder", "Moderov", "moder123", false, List.of(moderatorRole)));
        userRepository.save(createUser("user", "User", "Userov", "user123", false, List.of(userRole)));
        userRepository.save(createUser("ivan", "Ivan", "Petrov", "ivan123", false, List.of(userRole)));

        log.info("Data initialized successfully");
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }

    private User createUser(String username, String firstName, String lastName, String password, boolean isBlocked, List<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setIsBlocked(isBlocked);
        user.setRoles(roles);
        return user;
    }
}
