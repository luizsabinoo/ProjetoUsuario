package com.example.project_.service;

import com.example.project_.dtos.UserDto;
import com.example.project_.models.UserModel;
import com.example.project_.repositories.UserRepository;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<UserModel> saveUser(UserDto userDto) {

        if (userDto.age() < 18) {
            throw new IllegalArgumentException("Age must be at least 18");
        }


        if (!isEmailValid(userDto.email())) {
            throw new IllegalArgumentException("Invalid email");
        }

        UserModel user = new UserModel();
        BeanUtils.copyProperties(userDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }


    private boolean isEmailValid(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }



}
