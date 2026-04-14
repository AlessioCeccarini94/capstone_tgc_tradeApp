package alessioceccarini.tgcapp.config;

import alessioceccarini.tgcapp.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

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

			System.out.println("AUTH HEADER: " + authHeader);

			if (authHeader != null && authHeader.startsWith("Bearer ")) {

				String token = authHeader.substring(7);

				try {
					String username = jwtTools.extractUsername(token);

					System.out.println("SETTING PRINCIPAL: " + username);

					accessor.setUser(() -> username);

				} catch (Exception e) {
					System.out.println("JWT ERROR: " + e.getMessage());
				}

			} else {
				System.out.println("NO AUTH HEADER");
			}
		}

		return message;
	}
}