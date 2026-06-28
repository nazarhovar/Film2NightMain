package com.example.Film2NightMain.dto;

import java.util.List;

public class UserResponseDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isBlocked;
    private List<String> roles;

    public UserResponseDto(Long id, String username, String firstName, String lastName, Boolean isBlocked, List<String> roles) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isBlocked = isBlocked;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Boolean getIsBlocked() { return isBlocked; }
    public List<String> getRoles() { return roles; }
}
