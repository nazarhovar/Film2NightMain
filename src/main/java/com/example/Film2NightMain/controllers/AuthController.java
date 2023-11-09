package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.AuthDto;
import com.example.Film2NightMain.dto.RegisterDto;
import com.example.Film2NightMain.services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "Authentication")
public class AuthController {
    private final AuthService authService;

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthDto authDto) {
        return ResponseEntity.ok(authService.login(authDto));
    }

    @ApiOperation(value = "Register")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(authService.register(registerDto));
    }
}
