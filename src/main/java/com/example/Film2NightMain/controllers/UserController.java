package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "Юзеры")
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "Все юзеры")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @ApiOperation(value = "Найти юзера по id")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok().body(userService.findUserById(id));
    }
}
