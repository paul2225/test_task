package com.test.task.controller;

import com.test.task.domain.dao.User;
import com.test.task.domain.dto.UserDto;
import com.test.task.service.JwtService;
import com.test.task.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final JwtService jwtService;

    public RegistrationController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<String> register(@Validated UserDto userDto) {
        if(userService.getByUsername(userDto.getUsername()) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = new User(userDto.getUsername(), userService.encryptPassword(userDto.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(jwtService.createToken(user.getUsername()), HttpStatus.OK);
    }
}
