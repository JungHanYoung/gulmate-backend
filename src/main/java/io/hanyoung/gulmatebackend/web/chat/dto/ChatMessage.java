package io.hanyoung.gulmatebackend.web.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessage {

    private String message;
    private Long accountId;

    @Builder
    public ChatMessage(String message, Long accountId) {
        this.message = message;
        this.accountId = accountId;
    }
}
