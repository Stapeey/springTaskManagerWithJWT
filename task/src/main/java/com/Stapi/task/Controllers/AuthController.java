package com.Stapi.task.Controllers;

import com.Stapi.task.Dto.AuthDto;
import com.Stapi.task.Dto.TokenDto;
import com.Stapi.task.models.User;
import com.Stapi.task.repositories.UserRepository;
import com.Stapi.task.security.GenerateJWT;
import com.Stapi.task.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private GenerateJWT generateJWT;

    @Autowired
    private AuthController(GenerateJWT generateJWT, UserRepository userRepository, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.generateJWT = generateJWT;
    }

    @PostMapping("/register")
    private ResponseEntity<String> register(@RequestBody AuthDto authDto){
        if (userRepository.existsByUsername(authDto.getUsername())){
            return new ResponseEntity<>("Username is taken!", HttpStatus.CONFLICT);
        }
        User newUser = new User();
        newUser.setUsername(authDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(authDto.getPassword()));

        userRepository.save(newUser);
        return new ResponseEntity<>("Registered successfully!", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    private ResponseEntity<TokenDto> login(@RequestBody AuthDto authDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword()));
            TokenDto response = new TokenDto();
            response.setMessage("success");
            response.setToken(generateJWT.generateToken(authentication));
            response.setTokenType("Bearer ");
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        catch (BadCredentialsException e){
            TokenDto response = new TokenDto();
            response.setMessage("Wrong login information");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

    }
}
