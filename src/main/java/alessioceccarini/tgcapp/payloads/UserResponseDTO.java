package alessioceccarini.tgcapp.payloads;

import alessioceccarini.tgcapp.enums.Role;

import java.util.UUID;

public record UserResponseDTO(
		UUID userId,
		String firstName,
		String lastName,
		String username,
		String email,
		String city,
		Double rating,
		String image,
		Role role
) {
}
