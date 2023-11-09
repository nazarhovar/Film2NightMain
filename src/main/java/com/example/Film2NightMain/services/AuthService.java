package com.example.Film2NightMain.services;

import com.example.Film2NightMain.dto.AuthDto;
import com.example.Film2NightMain.dto.RegisterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface AuthService {
    ResponseEntity<?> login(AuthDto authenticationRequestDTO);
    ResponseEntity<?> register(@RequestBody RegisterDto registerDto);
}
