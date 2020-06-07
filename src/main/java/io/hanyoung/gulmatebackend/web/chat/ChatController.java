package io.hanyoung.gulmatebackend.web.chat;

import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.domain.chat.Chat;
import io.hanyoung.gulmatebackend.domain.chat.ChatRepository;
import io.hanyoung.gulmatebackend.domain.chat.dto.ChatMessageResponseDto;
import io.hanyoung.gulmatebackend.web.chat.dto.ChatMessage;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@Api(tags = "Gulmate Chatting")
public class ChatController {

    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;
    private final SimpMessagingTemplate template;

    @ApiOperation(value = "Get all chat messages", notes = "귤메이트에서 나눈 채팅 목록 전체 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "채팅 목록을 가져오는 데에 성공", response = ChatMessageResponseDto.class, responseContainer = "List"),
            @ApiResponse(code = 403, message = "인증된 사용자면서 귤메이트의 일원이어야 합니다."),
            @ApiResponse(code = 500, message = "서버 에러 발생")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", value = "귤메이트 ID", paramType = "path", dataTypeClass = Long.class, required = true)
    })
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

    @ApiOperation(value = "Get unread chat messages", notes = "읽지 않은 채팅 목록 조회", response = ChatMessageResponseDto.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", required = true, paramType = "path", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "chatMessageId", required = true, paramType = "query", dataTypeClass = Long.class)
    })
    @GetMapping("/api/v1/{familyId}/chat/unread")
    public ResponseEntity<?> unreadChat(
            @PathVariable Long familyId,
            @RequestParam Optional<Long> chatMessageId,
            @AuthUser Account account
    ) {
        if(account.getMemberInfos().stream()
                .noneMatch(memberInfo -> memberInfo.getFamily().getId().equals(familyId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(
                chatRepository.getUnreadChatMessageCount(familyId, chatMessageId.orElse(0L))
                        .stream()
                .map(ChatMessageResponseDto::new)
                .collect(Collectors.toList())
        );
    }

}
