package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.UserResponseDto;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Api(tags = "Users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "All users")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> users = userService.getUsers().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @ApiOperation(value = "Find user by id")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping("/user/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(toDto(userService.findUserById(id)));
    }

    private UserResponseDto toDto(User user) {
        List<String> roles = user.getRoles() != null
                ? user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList())
                : List.of();
        return new UserResponseDto(
                user.getId(), user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getIsBlocked(), roles
        );
    }
}
