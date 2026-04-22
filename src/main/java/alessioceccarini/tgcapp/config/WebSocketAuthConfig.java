package alessioceccarini.tgcapp.config;

import alessioceccarini.tgcapp.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.security.Principal;
import java.util.HashMap;

@Configuration
public class WebSocketAuthConfig implements ChannelInterceptor {
	private final JWTTools jwtTools;

	@Autowired
	public WebSocketAuthConfig(JWTTools jwtTools) {
		this.jwtTools = jwtTools;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String authHeader = accessor.getFirstNativeHeader("Authorization");

			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);

				try {
					String username = jwtTools.extractUsername(token);

					Principal principal = () -> username;
					accessor.setUser(principal);

					if (accessor.getSessionAttributes() == null) {
						accessor.setSessionAttributes(new HashMap<>());
					}
					accessor.getSessionAttributes().put("username", username);

				} catch (Exception e) {
					throw new IllegalArgumentException("Invalid JWT token", e);
				}
			}
		}

		return message;
	}
}
