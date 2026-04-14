package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.exceptions.UnauthorizedException;
import alessioceccarini.tgcapp.payloads.LoginDTO;
import alessioceccarini.tgcapp.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final UserService userService;
	private final JWTTools jwtTools;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AuthenticationService(UserService userService, JWTTools jwtTools, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.jwtTools = jwtTools;
		this.passwordEncoder = passwordEncoder;

	}

	public String checkCredentialsAndReturnToken(LoginDTO loginDTO) {
		// CHECK CREDENTIALS
		User user = this.userService.findByEmail(loginDTO.email());
		if (passwordEncoder.matches(loginDTO.password(), user.getPassword())) {
			String accessToken = jwtTools.generateToken(user);
			return accessToken;
		} else {
			throw new UnauthorizedException("Invalid credentials");
		}
	}
}
