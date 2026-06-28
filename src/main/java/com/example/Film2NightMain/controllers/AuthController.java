package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.AuthDto;
import com.example.Film2NightMain.dto.AuthResponseDto;
import com.example.Film2NightMain.dto.RegisterDto;
import com.example.Film2NightMain.errors.InvalidCredentialsException;
import com.example.Film2NightMain.errors.UsernameAlreadyExistsException;
import com.example.Film2NightMain.services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api(tags = "Authentication")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto authDto) {
        try {
            AuthResponseDto response = authService.login(authDto);
            return ResponseEntity.ok(response);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Register")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        try {
            authService.register(registerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registered successfully");
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
