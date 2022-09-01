package com.test.task.service;

public interface JwtService {
    String createToken(String username);
    boolean verifyToken(String username, String token);
}
