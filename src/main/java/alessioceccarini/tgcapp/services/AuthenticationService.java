package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.exceptions.UnauthorizedException;
import alessioceccarini.tgcapp.payloads.LoginDTO;
import alessioceccarini.tgcapp.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final UserService userService;
	private final JWTTools jwtTools;

	@Autowired
	public AuthenticationService(UserService userService, JWTTools jwtTools) {
		this.userService = userService;
		this.jwtTools = jwtTools;

	}

	public String checkCredentialsAndReturnToken(LoginDTO loginDTO) {
		// CHECK CREDENTIALS
		User user = this.userService.findByEmail(loginDTO.email());
		if (user.getPassword().equals(loginDTO.password())) {

			// GENERATE TOKEN

			String accessToken = jwtTools.genearateToken(user);

			// RETURN TOKEN
			return accessToken;
			// UNAUTHORIZED (?)

		} else {
			throw new UnauthorizedException("Invalid credentials");
		}
	}
}
