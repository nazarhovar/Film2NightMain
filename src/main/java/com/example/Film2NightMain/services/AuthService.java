package com.example.Film2NightMain.services;

import com.example.Film2NightMain.dto.AuthDto;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    ResponseEntity<?> login(AuthDto authenticationRequestDTO);
}
