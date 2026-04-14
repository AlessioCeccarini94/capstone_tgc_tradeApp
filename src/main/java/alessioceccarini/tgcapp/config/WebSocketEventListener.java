package alessioceccarini.tgcapp.config;

import alessioceccarini.tgcapp.enums.MessageType;
import alessioceccarini.tgcapp.payloads.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
	private final SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	public WebSocketEventListener(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@EventListener
	public void handleDisconnect(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccessor.getSessionAttributes().get("username");

		if (username != null) {
			MessageDTO messageDTO = new MessageDTO(MessageType.LEAVE, username, null, null);
			simpMessagingTemplate.convertAndSend("/topic/public", messageDTO);
		}
	}
}
