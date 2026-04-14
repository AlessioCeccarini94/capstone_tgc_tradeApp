package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.payloads.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
	private final SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public MessageDTO sendMessage(@Payload MessageDTO message) {
		return message;
	}

	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public MessageDTO addUser(@Payload MessageDTO message, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", message.sender());
		return message;
	}

	@MessageMapping("/chat.private")
	public void privateMessage(@Payload MessageDTO message) {

		System.out.println("=== MESSAGE DEBUG ===");
		System.out.println("SENDER: " + message.sender());
		System.out.println("RECEIVER: " + message.receiver());

		simpMessagingTemplate.convertAndSendToUser(
				message.receiver(),
				"/queue/messages",
				message
		);

		System.out.println("MESSAGE SENT TO USER CHANNEL");
	}
}
