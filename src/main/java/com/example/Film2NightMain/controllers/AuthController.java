package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.AuthDto;
import com.example.Film2NightMain.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthDto authDto) {
        return ResponseEntity.ok(authService.login(authDto));
    }
}
