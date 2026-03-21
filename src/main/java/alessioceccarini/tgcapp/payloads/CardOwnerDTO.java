package alessioceccarini.tgcapp.payloads;

import java.util.UUID;

public record CardOwnerDTO(
		UUID userId,
		String username,
		Double rating,
		String email
) {
}
