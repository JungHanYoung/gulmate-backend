package io.hanyoung.gulmatebackend.config.web.socket;

import io.hanyoung.gulmatebackend.config.auth.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtValidateInterceptor implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.CONNECT == accessor.getCommand()) {
            String jwt = accessor.getFirstNativeHeader("Authorization");
            if(StringUtils.hasText(jwt) && jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
                System.out.println("HelloWorld@@@@@@ : " + jwt);
            }
//            try {
//                Map<String, Object> verify = jwtUtils.verify(jwt);
//            } catch(Exception e) {
//                throw new IllegalArgumentException();
//            }
        }
        return message;
    }
}
