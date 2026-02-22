package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.exceptions.UnauthorizedException;
import alessioceccarini.tgcapp.payloads.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final UserService userService;

	@Autowired
	public AuthenticationService(UserService userService) {
		this.userService = userService;
	}

	public String checkCredentialsAndReturnToken(LoginDTO loginDTO) {
		// CHECK CREDENTIALS
		User user = this.userService.findByEmail(loginDTO.email());
		if (user.getPassword().equals(loginDTO.password())) {

		} else {
			throw new UnauthorizedException("Invalid credentials");
		}

		// GENERATE TOKEN

		// RETURN TOKEN

		// UNAUTHORIZED (?)
	}
}
