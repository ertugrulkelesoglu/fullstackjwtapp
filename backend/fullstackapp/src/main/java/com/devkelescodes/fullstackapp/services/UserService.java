package com.devkelescodes.fullstackapp.services;

import com.devkelescodes.fullstackapp.dto.CredentialsDto;
import com.devkelescodes.fullstackapp.dto.SignUpDto;
import com.devkelescodes.fullstackapp.dto.UserDto;
import com.devkelescodes.fullstackapp.entity.User;
import com.devkelescodes.fullstackapp.exceptions.AppException;
import com.devkelescodes.fullstackapp.mappers.UserMapper;
import com.devkelescodes.fullstackapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }


    public UserDto login(CredentialsDto credentialsDto) {
        User user =  userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        if(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
                user.getPassword())) {
            return userMapper.userToUserDto(user);
        }
        throw new AppException("Invalid request", HttpStatus.BAD_REQUEST);
        }

    public UserDto register(SignUpDto signUpDto) {
        Optional<User> ouser = userRepository.findByLogin(signUpDto.login());

        if(ouser.isPresent()) {
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(user);

    }


}
