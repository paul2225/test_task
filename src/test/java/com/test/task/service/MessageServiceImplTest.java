package com.test.task.service;

import com.test.task.domain.dao.Message;
import com.test.task.domain.dao.User;
import com.test.task.domain.dto.MessageDto;
import com.test.task.repository.MessageRepository;
import com.test.task.repository.UserRepository;
import com.test.task.service.impl.MessageServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceImplTest {
    private static final String USER_NAME = "someName";
    private static final String PASSWORD = "somePass";
    private static final String TEXT_1 = "someText1";
    private static final String TEXT_2 = "someText2";
    private static final String TEXT_3 = "someText3";
    private static final String TEXT_4 = "someText4";

    private static final String HISTORY_2 = "history 2";

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private List<Message> messages = new ArrayList<>();

    @Before
    public void init() {
        user = new User(USER_NAME, userService.encryptPassword(PASSWORD));
        user = userService.save(user);
        Message message1 = new Message(TEXT_1, LocalDateTime.now(), user);
        message1 = messageService.save(message1);
        Message message2 = new Message(TEXT_2, LocalDateTime.now(), user);
        message2 = messageService.save(message2);
        Message message3 = new Message(TEXT_3, LocalDateTime.now(), user);
        message3 = messageService.save(message3);

        messages.add(message3);
        messages.add(message2);
        messages.add(message1);
    }

    @After
    public void cleanup() {
        messageRepository.deleteAll(messages);
        userRepository.delete(user);
    }

    @Test
    public void saveReturnsSameMessage() {
        Message message = messages.get(0);
        Message messageFromDb = messageRepository.findById(message.getId()).get();

        assertEquals(message.getId(), messageFromDb.getId());
        assertEquals(message.getText(), messageFromDb.getText());
    }

    @Test
    public void findLast0MessagesReturnsEmptyList() {
        assertTrue(messageService.findLastNMessages(0).isEmpty());
    }

    @Test
    public void findLastNegativeNumberMessagesReturnsEmptyList() {
        assertTrue(messageService.findLastNMessages(-2).isEmpty());
    }

    @Test
    public void findLastMoreThanExistAmountMessagesReturnsAllMessages() {
        List<Message> messagesFromDb = messageService.findLastNMessages(123);

        assertEquals(messages.size(), messagesFromDb.size());
        for (int i = 0; i < 3; i++) {
            Message message = messages.get(i);
            Message messageFromDb = messagesFromDb.get(i);

            assertEquals(message.getId(), messageFromDb.getId());
            assertEquals(message.getText(), messageFromDb.getText());
        }
    }

    @Test
    public void findLastLessThanExistAmountMessagesReturnsValidMessages() {
        List<Message> messagesFromDb = messageService.findLastNMessages(2);

        assertNotEquals(messages.size(), messagesFromDb.size());
        for (int i = 0; i < 2; i++) {
            Message message = messages.get(i);
            Message messageFromDb = messagesFromDb.get(i);

            assertEquals(message.getId(), messageFromDb.getId());
            assertEquals(message.getText(), messageFromDb.getText());
        }
    }

    @Test
    public void handleMessageReturnsHistoryWhenMessageMatchesPattern() {
        List<MessageDto> messagesFromHandleMessageMethod = messageService.handleMessage(new MessageDto(USER_NAME, HISTORY_2, LocalDateTime.now()));

        assertNotEquals(messages.size(), messagesFromHandleMessageMethod.size());
        for (int i = 0; i < 2; i++) {
            Message message = messages.get(i);
            MessageDto messageFromDb = messagesFromHandleMessageMethod.get(i);

            assertEquals(message.getUser().getUsername(), messageFromDb.getUsername());
            assertEquals(message.getText(), messageFromDb.getText());
        }
    }

    @Test
    public void handleMessageSavesMessageWhenMessageNotMatchesPattern() {
        MessageDto messageDto = new MessageDto(USER_NAME, TEXT_4, LocalDateTime.now());
        messageService.handleMessage(messageDto);

        Message message = messageService.findLastNMessages(1).get(0);

        assertEquals(message.getText(), messageDto.getText());
        assertEquals(message.getUser().getUsername(), messageDto.getUsername());

        messageRepository.delete(message);
    }
}
