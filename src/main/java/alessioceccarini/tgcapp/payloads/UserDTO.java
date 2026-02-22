package alessioceccarini.tgcapp.payloads;

import alessioceccarini.tgcapp.enums.Role;
import jakarta.validation.constraints.*;

public record UserDTO(
		@NotBlank
		String firstName,
		@NotBlank
		String lastName,
		@NotBlank
		@Size(min = 3, max = 20)
		String username,
		@NotBlank
		@Email
		String email,
		@NotBlank
		@Size(min = 8)
		@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*)(?=.*[0-9][@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$",
				message = "la password deve contenere almeno 8 caratteri,una lettera maiuscola, una minuscola, un numero e un carattere speciale")
		String password,
		@NotNull
		Role role
) {
}
