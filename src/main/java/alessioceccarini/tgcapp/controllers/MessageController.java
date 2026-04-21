package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.payloads.MessageDTO;
import alessioceccarini.tgcapp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
