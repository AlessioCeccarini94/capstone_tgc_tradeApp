package alessioceccarini.tgcapp.payloads;

import alessioceccarini.tgcapp.enums.MessageType;

import java.time.LocalDate;

public record MessageDTO(
		String message,
		String sender,
		String receiver,
		MessageType type,
		LocalDate date

) {
}
