package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.AuthDto;
import com.example.Film2NightMain.dto.AuthResponseDto;
import com.example.Film2NightMain.dto.RegisterDto;
import com.example.Film2NightMain.entities.Role;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.errors.InvalidCredentialsException;
import com.example.Film2NightMain.errors.UsernameAlreadyExistsException;
import com.example.Film2NightMain.repositories.RoleRepository;
import com.example.Film2NightMain.repositories.UserRepository;
import com.example.Film2NightMain.security.JwtTokenProvider;
import com.example.Film2NightMain.services.AuthService;
import com.example.Film2NightMain.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthServiceImpl.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider, UserService userService,
                           BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                           RoleRepository roleRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public AuthResponseDto login(AuthDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = userService.findByUsername(username);

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        String token = jwtTokenProvider.createToken(username, user.getRoles());

        AuthResponseDto responseDto = new AuthResponseDto();
        responseDto.setUsername(username);
        responseDto.setToken(token);

        return responseDto;
    }

    @Override
    @Transactional
    public void register(RegisterDto registerDto) {
        String username = registerDto.getUsername();

        if (userService.findByUsernameRegister(username) != null) {
            throw new UsernameAlreadyExistsException("Username already exists: " + username);
        }

        String hashedPassword = bCryptPasswordEncoder.encode(registerDto.getPassword());

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setFirstName(registerDto.getFirstName());
        newUser.setLastName(registerDto.getLastName());
        newUser.setIsBlocked(false);

        Role userRole = roleRepository.findByName("USER");
        newUser.setRoles(List.of(userRole));

        log.info("Saving user: {}", newUser.getUsername());
        userRepository.save(newUser);
    }
}
