package alessioceccarini.tgcapp.config;

import alessioceccarini.tgcapp.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketAuthConfig implements ChannelInterceptor {
	private final JWTTools jwtTools;

	@Autowired
	public WebSocketAuthConfig(JWTTools jwtTools) {
		this.jwtTools = jwtTools;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor =
				MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (accessor == null) {
			return message;
		}

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String authHeader = accessor.getFirstNativeHeader("Authorization");
			System.out.println("WS CONNECT Authorization: " + authHeader);

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				throw new IllegalArgumentException("Missing or invalid Authorization header");
			}

			String token = authHeader.substring(7);
			String username = jwtTools.extractUsername(token);
			System.out.println("WS authenticated user: " + username);

			Principal principal = () -> username;
			accessor.setUser(principal);

			Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
			if (sessionAttributes == null) {
				sessionAttributes = new HashMap<>();
				accessor.setSessionAttributes(sessionAttributes);
			}
			sessionAttributes.put("username", username);

			accessor.setLeaveMutable(true);
			return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
		}

		return message;
	}
}
