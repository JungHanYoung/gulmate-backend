package io.hanyoung.gulmatebackend.config.socket;

import io.hanyoung.gulmatebackend.config.auth.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(message);
//        if(wrap.getDestination() != null && wrap.getDestination().contains("/"))
        if(wrap.getCommand() == StompCommand.CONNECT) {
            String authorizationHeader = wrap.getFirstNativeHeader("Authorization");
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                try {
                    Map<String, Object> verify = jwtUtils.verify(token);
                    Integer accountId = (Integer) verify.get("id");
                    System.out.println("accountId: " + accountId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if(wrap.getCommand() == StompCommand.SEND) {
            System.out.println("Stomp Send : " + wrap.getDestination());
        }
        return message;
    }
}
