package alessioceccarini.tgcapp.payloads;

import java.time.LocalDate;

public record ConversationDTO(
		String chatKey,
		String otherUser,
		String lastMessage,
		LocalDate date
) {
}
