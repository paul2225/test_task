package com.test.task.service;

import com.test.task.service.impl.JwtServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class JwtServiceImplTest {
    private final static String NAME = "someName";
    private final static String OTHER_NAME = "someName";

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Test
    public void whenNameCorrespondsTokenThenReturnTrue() {
        String token = jwtService.createToken(NAME);
        assertTrue("Token wasn't verified", jwtService.verifyToken(NAME, token));
    }

    @Test
    public void whenNameNotCorrespondsTokenThenReturnFalse() {
        String token = jwtService.createToken(NAME);
        assertTrue("Token was verified", jwtService.verifyToken(OTHER_NAME, token));
    }
}
