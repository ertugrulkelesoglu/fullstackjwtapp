package com.devkelescodes.fullstackapp.controllers;

import com.devkelescodes.fullstackapp.config.UserAuthProvider;
import com.devkelescodes.fullstackapp.dto.CredentialsDto;
import com.devkelescodes.fullstackapp.dto.SignUpDto;
import com.devkelescodes.fullstackapp.dto.UserDto;
import com.devkelescodes.fullstackapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class AuthController {
    @Autowired
    private  UserService userService;

    @Autowired
    private UserAuthProvider userAuthProvider;
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userService.login(credentialsDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        UserDto user = userService.register(signUpDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }
}
