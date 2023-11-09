package com.example.Film2NightMain.dto;

import lombok.Data;

@Data
public class RegisterDto {
    public String username;
    public String password;
    private String firstName;
    private String lastName;
}
