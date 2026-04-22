package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.payloads.MessageDTO;
import alessioceccarini.tgcapp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class ChatController {
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final MessageService messageService;

	@Autowired
	public ChatController(SimpMessagingTemplate simpMessagingTemplate,
						  MessageService messageService) {
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.messageService = messageService;
	}

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public MessageDTO sendMessage(@Payload MessageDTO message) {
		return message;
	}

	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public MessageDTO addUser(@Payload MessageDTO message, SimpMessageHeaderAccessor headerAccessor) {
		if (headerAccessor.getSessionAttributes() != null) {
			headerAccessor.getSessionAttributes().put("username", message.sender());
		}
		return message;
	}

	@MessageMapping("/chat.private")
	public void privateMessage(@Payload MessageDTO message, Principal principal) {
		if (principal == null) {
			throw new IllegalStateException("Unauthorized websocket session");
		}

		String authenticatedSender = principal.getName();

		MessageDTO safeMessage = new MessageDTO(
				message.message(),
				authenticatedSender,
				message.receiver(),
				message.type(),
				message.date() != null ? message.date() : LocalDate.now()
		);

		messageService.saveMessage(safeMessage);

		simpMessagingTemplate.convertAndSendToUser(
				safeMessage.receiver(),
				"/queue/messages",
				safeMessage
		);

		simpMessagingTemplate.convertAndSendToUser(
				safeMessage.sender(),
				"/queue/messages",
				safeMessage
		);
	}
}
