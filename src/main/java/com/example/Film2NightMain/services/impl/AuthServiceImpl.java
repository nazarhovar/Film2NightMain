package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.AuthDto;
import com.example.Film2NightMain.dto.AuthResponseDto;
import com.example.Film2NightMain.entities.User;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
}
