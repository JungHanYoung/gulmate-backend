package io.hanyoung.gulmatebackend.web.chat;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.domain.chat.Chat;
import io.hanyoung.gulmatebackend.domain.chat.ChatRepository;
import io.hanyoung.gulmatebackend.domain.chat.dto.ChatMessageResponseDto;
import io.hanyoung.gulmatebackend.web.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;
    private final SimpMessagingTemplate template;

    @GetMapping("/api/v1/{familyId}/chat")
    public ResponseEntity<?> getAllFamilyChatMessageList(@PathVariable Long familyId) {

        List<Chat> chatMessageListByFamilyId = chatRepository.getChatMessageListByFamilyId(familyId);

        return ResponseEntity.ok(chatMessageListByFamilyId.stream().map(ChatMessageResponseDto::new).collect(Collectors.toList()));
    }

    @MessageMapping("/{familyId}/chat")
    public void chatting(@DestinationVariable Long familyId, ChatMessage chatMessage) {
        Account account = accountRepository.findById(chatMessage.getAccountId())
            .orElseThrow(() -> new IllegalArgumentException("Error: It is "));
        if(account.getCurrentFamily().getId().equals(familyId)) {
            chatRepository.save(Chat.builder()
                    .message(chatMessage.getMessage())
                    .account(account)
                    .family(account.getCurrentFamily())
                    .build());

            template.convertAndSend("/sub/family/" + familyId, chatMessage);
        }
    }

}
