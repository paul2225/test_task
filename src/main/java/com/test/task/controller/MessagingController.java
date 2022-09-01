package com.test.task.controller;

import com.test.task.domain.dto.MessageDto;
import com.test.task.service.JwtService;
import com.test.task.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessagingController {

    private final MessageService messageService;
    private final JwtService jwtService;

    public MessagingController(MessageService messageService, JwtService jwtService) {
        this.messageService = messageService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<List<MessageDto>> message(@RequestHeader("authorization") String token, @Validated MessageDto messageDto) {
        if(!jwtService.verifyToken(messageDto.getUsername(), token)) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(messageService.handleMessage(messageDto), HttpStatus.OK);
    }
}
