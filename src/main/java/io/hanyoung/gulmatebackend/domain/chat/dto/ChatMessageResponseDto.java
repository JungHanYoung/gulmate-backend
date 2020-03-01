package io.hanyoung.gulmatebackend.domain.chat.dto;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.chat.Chat;
import io.hanyoung.gulmatebackend.web.account.dto.AccountResponseDto;
import lombok.Getter;

@Getter
public class ChatMessageResponseDto {

    private String message;
    private AccountResponseDto creator;

    public ChatMessageResponseDto(Chat chatMessage) {
        this.message = chatMessage.getMessage();
        this.creator = new AccountResponseDto(chatMessage.getAccount());
    }
}
