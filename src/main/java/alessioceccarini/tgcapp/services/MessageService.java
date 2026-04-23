package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.Message;
import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.payloads.ConversationDTO;
import alessioceccarini.tgcapp.payloads.MessageDTO;
import alessioceccarini.tgcapp.repositories.MessageRepo;
import alessioceccarini.tgcapp.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

	public List<ConversationDTO> getUserConversations(String username) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		List<Message> messages = messageRepo.findAllUserMessages(user);
		Map<String, ConversationDTO> conversations = new LinkedHashMap<>();

		for (Message message : messages) {
			String sender = message.getSender().getUsername();
			String receiver = message.getReceiver().getUsername();
			String otherUser = sender.equals(username) ? receiver : sender;
			String chatKey = buildChatKey(username, otherUser);

			if (!conversations.containsKey(chatKey)) {
				conversations.put(chatKey, new ConversationDTO(
						chatKey,
						otherUser,
						message.getMessage(),
						message.getTimestamp()
				));
			}
		}

		return conversations.values().stream().toList();
	}

	@Transactional
	public void deleteConversation(String username, String otherUsername) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		User otherUser = userRepo.findByUsername(otherUsername)
				.orElseThrow(() -> new RuntimeException("User not found"));

		messageRepo.deleteConversation(user, otherUser);
	}

	private String buildChatKey(String user1, String user2) {
		return user1.compareTo(user2) <= 0
				? user1 + "||" + user2
				: user2 + "||" + user1;
	}
}
