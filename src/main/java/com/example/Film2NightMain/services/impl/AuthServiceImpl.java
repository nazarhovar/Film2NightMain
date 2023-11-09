package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.AuthDto;
import com.example.Film2NightMain.dto.AuthResponseDto;
import com.example.Film2NightMain.dto.RegisterDto;
import com.example.Film2NightMain.entities.Role;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.UserRepository;
import com.example.Film2NightMain.security.JwtTokenProvider;
import com.example.Film2NightMain.services.AuthService;
import com.example.Film2NightMain.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public ResponseEntity<?> login(@RequestBody AuthDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username");
        }

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            String token = jwtTokenProvider.createToken(username, user.getRoles());

            AuthResponseDto responseDto = new AuthResponseDto();
            responseDto.setUsername(username);
            responseDto.setToken(token);

            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid password");
        }
    }

    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        try {
            String username = registerDto.getUsername();
            String password = registerDto.getPassword();

            if (userService.findByUsernameRegister(username) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Username already exists");
            }

            String hashedPassword = bCryptPasswordEncoder.encode(password);

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(hashedPassword);
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setIsBlocked(false);

            Role defaultRole = new Role();
            defaultRole.setId(2L);
            newUser.setRoles(List.of(defaultRole));

            log.info("Saving user: " + newUser.getUsername());
            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

}
