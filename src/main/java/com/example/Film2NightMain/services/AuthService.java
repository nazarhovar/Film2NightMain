package com.example.Film2NightMain.services;

import com.example.Film2NightMain.dto.AuthDto;
import com.example.Film2NightMain.dto.AuthResponseDto;
import com.example.Film2NightMain.dto.RegisterDto;

public interface AuthService {
    AuthResponseDto login(AuthDto authDto);
    void register(RegisterDto registerDto);
}
