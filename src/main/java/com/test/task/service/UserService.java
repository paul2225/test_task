package com.test.task.service;

import com.test.task.domain.dao.User;
import com.test.task.domain.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User getByUsername(String username);
    User save(User user);
    String encryptPassword(String password);
    ResponseEntity<String> authorizeUser(UserDto userDto);
}
