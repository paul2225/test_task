package com.test.task.controller;

import com.test.task.domain.dto.UserDto;
import com.test.task.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
public class AuthenticationControllerTest {
    private static final String VALID_NAME = "someName";
    private static final String INVALID_NAME = null;
    private static final String VALID_PASSWORD = "somePassword";
    private static final String INVALID_PASSWORD = null;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BCrypt bCrypt;

    @Autowired
    private AuthenticationController authenticationController;

    @Test
    public void contextLoads() {
        assertThat(authenticationController).isNotNull();
    }

    @Test
    public void whenInvalidUsernameThenReturn400() throws Exception{
        mockMvc.perform(post("/authentication")
                        .param("username", INVALID_NAME)
                        .param("password", VALID_PASSWORD))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInvalidPasswordThenReturn400() throws Exception{
        mockMvc.perform(post("/authentication")
                        .param("username", VALID_NAME)
                        .param("password", INVALID_PASSWORD))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInputThenReturn200() throws Exception{
        when(userService.authorizeUser(new UserDto(VALID_NAME, VALID_PASSWORD))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post("/authentication")
                        .param("username", VALID_NAME)
                        .param("password", VALID_PASSWORD))
                .andExpect(status().isOk());
    }
}
