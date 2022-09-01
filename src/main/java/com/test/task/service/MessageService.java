package com.test.task.service;

import com.test.task.domain.dao.Message;
import com.test.task.domain.dto.MessageDto;

import java.util.List;

public interface MessageService {
    Message save(Message message);
    List<Message> findLastNMessages(int n);
    List<MessageDto> handleMessage(MessageDto messageDto);
}
