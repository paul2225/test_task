package com.test.task.service.impl;

import com.test.task.domain.dao.User;
import com.test.task.domain.dto.UserDto;
import com.test.task.repository.UserRepository;
import com.test.task.service.JwtService;
import com.test.task.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public ResponseEntity<String> authorizeUser(UserDto userDto) {
        User user = getByUsername(userDto.getUsername());
        if(user == null || !BCrypt.checkpw(userDto.getPassword(), user.getEncryptedPassword())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(jwtService.createToken(user.getUsername()), HttpStatus.OK);
    }
}
