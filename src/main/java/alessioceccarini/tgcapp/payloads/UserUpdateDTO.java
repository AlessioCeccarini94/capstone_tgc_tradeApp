package alessioceccarini.tgcapp.payloads;

import java.util.UUID;

public record UserUpdateDTO(
		String firstName,
		String lastName,
		String username,
		String email,
		UUID cityId
) {
}
