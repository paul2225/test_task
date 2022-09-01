package com.test.task.service;

import com.test.task.domain.dao.User;
import com.test.task.domain.dto.UserDto;
import com.test.task.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    private static final String NAME = "someName";
    private static final String PASSWORD = "somePass";
    private static final String PASSWORD_2 = "somePass2";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void init() {
        user = new User(NAME, userService.encryptPassword(PASSWORD));
        user = userService.save(user);
    }

    @After
    public void cleanup() {
        userRepository.delete(user);
    }

    @Test
    public void getByUsernameReturnsValidUser() {
        User userFromDb = userService.getByUsername(NAME);

        assertEquals(user.getUsername(), userFromDb.getUsername());
        assertTrue(BCrypt.checkpw(PASSWORD, userFromDb.getEncryptedPassword()));
    }

    @Test
    public void encryptPasswordReturnSameWhenProvidedSameString() {
        String encrypted1 = userService.encryptPassword(PASSWORD);
        String encrypted2 = userService.encryptPassword(PASSWORD);

        assertTrue(BCrypt.checkpw(PASSWORD, encrypted1));
        assertTrue(BCrypt.checkpw(PASSWORD, encrypted2));
    }

    @Test
    public void encryptPasswordReturnDifferentWhenProvidedDifferentString() {
        String encrypted1 = userService.encryptPassword(PASSWORD);
        String encrypted2 = userService.encryptPassword(PASSWORD_2);

        assertTrue(BCrypt.checkpw(PASSWORD, encrypted1));
        assertFalse(BCrypt.checkpw(PASSWORD, encrypted2));
        assertTrue(BCrypt.checkpw(PASSWORD_2, encrypted2));
        assertFalse(BCrypt.checkpw(PASSWORD_2, encrypted1));
    }

    @Test
    public void authorizeUserWhenWrongPasswordReturnsUnauthorized() {
        ResponseEntity<String> responseEntity = userService.authorizeUser(new UserDto(NAME, PASSWORD_2));
        assertTrue(responseEntity.getStatusCode().is4xxClientError());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void authorizeUserWhenValidPasswordReturnsSuccess() {
        ResponseEntity<String> responseEntity = userService.authorizeUser(new UserDto(NAME, PASSWORD));
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        jwtService.verifyToken(NAME, responseEntity.getBody());
    }
}
