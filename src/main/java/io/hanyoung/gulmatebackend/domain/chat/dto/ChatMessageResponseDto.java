package io.hanyoung.gulmatebackend.domain.chat.dto;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.chat.Chat;
import lombok.Getter;

@Getter
public class ChatMessageResponseDto {

    private String message;
    private Account creator;

    public ChatMessageResponseDto(Chat chatMessage) {
        this.message = chatMessage.getMessage();
        this.creator = chatMessage.getAccount();
    }
}
