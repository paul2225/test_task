package com.test.task.domain.dto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class MessageDto {
    @NotBlank
    private String username;
    private String text;
    private LocalDateTime date;

    public MessageDto(String username, String text, LocalDateTime date) {
        this.username = username;
        this.text = text;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
