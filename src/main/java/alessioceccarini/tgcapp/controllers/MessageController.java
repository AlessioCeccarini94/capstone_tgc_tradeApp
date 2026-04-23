package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.payloads.ConversationDTO;
import alessioceccarini.tgcapp.payloads.MessageDTO;
import alessioceccarini.tgcapp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
	private final MessageService messageService;

	@Autowired
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping("/{sender}/{receiver}")
	public List<MessageDTO> getConversation(@PathVariable String sender,
											@PathVariable String receiver) {
		return messageService.getConversation(sender, receiver);
	}

	@GetMapping("/conversations/{username}")
	public List<ConversationDTO> getUserConversations(@PathVariable String username) {
		return messageService.getUserConversations(username);
	}

	@DeleteMapping("/{username}/{otherUsername}")
	public void deleteConversation(@PathVariable String username,
								   @PathVariable String otherUsername) {
		messageService.deleteConversation(username, otherUsername);
	}
}
