package com.test.task.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.test.task.service.JwtService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger LOGGER = Logger.getLogger(JwtServiceImpl.class.getName());

    @Override
    public String createToken(String username) {
        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256("cHuioHUIOhuioHIPhIOPHP");
            token = JWT.create()
                    .withClaim("name", username)
                    .withIssuer("testTask")
                    .withExpiresAt(new Date(System.currentTimeMillis() + (5 * 60 * 1000)))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            LOGGER.warning(exception.getMessage());
        }
        return token;
    }

    @Override
    public boolean verifyToken(String username, String token) {
        token = token.replace("Bearer_", "");
        DecodedJWT jwt;
        try {
            Algorithm algorithm = Algorithm.HMAC256("cHuioHUIOhuioHIPhIOPHP");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("testTask")
                    .build();
            jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            LOGGER.warning(exception.getMessage());
            return false;
        }

        return jwt.getClaim("name").asString().equals(username);
    }
}
