package com.test.task.controller;

import com.test.task.service.JwtService;
import com.test.task.service.MessageService;
import com.test.task.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MessagingController.class)
public class MessagingControllerTest {
    private static final String VALID_NAME = "someName";
    private static final String TOKEN = "validToken";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private JwtService jwtService;
    @Autowired
    private MessagingController messagingController;

    @Test
    public void contextLoads() {
        assertThat(messagingController).isNotNull();
    }

    @Test
    public void whenInvalidTokenThenReturn401() throws Exception{
        when(jwtService.verifyToken(VALID_NAME, TOKEN)).thenReturn(false);
        mockMvc.perform(post("/message")
                        .param("username", VALID_NAME)
                        .header("authorization", TOKEN))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenNoTokenThenReturn400() throws Exception{
        when(jwtService.verifyToken(VALID_NAME, TOKEN)).thenReturn(false);
        mockMvc.perform(post("/message")
                        .param("username", VALID_NAME))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidTokenThenReturn200() throws Exception{
        when(jwtService.verifyToken(VALID_NAME, TOKEN)).thenReturn(true);
        mockMvc.perform(post("/message")
                        .param("username", VALID_NAME)
                        .header("authorization", TOKEN))
                .andExpect(status().isOk());
    }
}
