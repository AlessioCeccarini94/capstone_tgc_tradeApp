package alessioceccarini.tgcapp.payloads;

import java.util.UUID;

public record UserResponseDTO(
		UUID userId,
		String firstName,
		String lastName,
		String username,
		String email,
		Double rating,
		String image
) {
}
