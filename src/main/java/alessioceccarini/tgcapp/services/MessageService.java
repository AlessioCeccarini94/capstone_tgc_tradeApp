package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.Message;
import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.payloads.MessageDTO;
import alessioceccarini.tgcapp.repositories.MessageRepo;
import alessioceccarini.tgcapp.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MessageService {
	private final MessageRepo messageRepo;
	private final UserRepo userRepo;

	@Autowired
	public MessageService(MessageRepo messageRepo, UserRepo userRepo) {
		this.messageRepo = messageRepo;
		this.userRepo = userRepo;
	}

	public Message saveMessage(MessageDTO dto) {
		User sender = userRepo.findByUsername(dto.sender())
				.orElseThrow(() -> new RuntimeException("Sender not found"));
		User receiver = userRepo.findByUsername(dto.receiver())
				.orElseThrow(() -> new RuntimeException("Receiver not found"));

		Message message = new Message();
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setMessage(dto.message());
		message.setType(dto.type());
		message.setTimestamp(dto.date() != null ? dto.date() : LocalDate.now());

		return messageRepo.save(message);
	}

	public List<MessageDTO> getConversation(String senderUsername, String receiverUsername) {
		User sender = userRepo.findByUsername(senderUsername)
				.orElseThrow(() -> new RuntimeException("User not found"));
		User receiver = userRepo.findByUsername(receiverUsername)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return messageRepo.findConversation(sender, receiver).stream()
				.map(m -> new MessageDTO(
						m.getMessage(),
						m.getSender().getUsername(),
						m.getReceiver().getUsername(),
						m.getType(),
						m.getTimestamp()
				))
				.toList();
	}
}
