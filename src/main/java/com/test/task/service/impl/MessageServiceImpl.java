package com.test.task.service.impl;

import com.test.task.domain.dao.Message;
import com.test.task.domain.dto.MessageDto;
import com.test.task.repository.MessageRepository;
import com.test.task.service.MessageService;
import com.test.task.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public MessageServiceImpl(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findLastNMessages(int n) {
        if(n < 0) return Collections.emptyList();
        return messageRepository.findFirstNMessages(n);
    }

    @Override
    public List<MessageDto> handleMessage(MessageDto messageDto) {
        if(messageDto.getText().matches("history \\d+")) {
            int n = Integer.parseInt(messageDto.getText().trim().split("\\s+")[1]);
            return findLastNMessages(n).stream()
                    .map(a -> new MessageDto(a.getUser().getUsername(), a.getText(), a.getDate()))
                    .collect(Collectors.toList());
        }

        save(new Message(messageDto.getText(), LocalDateTime.now(), userService.getByUsername(messageDto.getUsername())));
        return Collections.emptyList();
    }
}
