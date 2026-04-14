package alessioceccarini.tgcapp.payloads;

import alessioceccarini.tgcapp.enums.MessageType;

public record MessageDTO(
		MessageType type,
		String sender,
		String receiver,
		String content
) {
}
