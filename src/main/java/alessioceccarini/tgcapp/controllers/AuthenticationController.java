package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.payloads.LoginAccessDTO;
import alessioceccarini.tgcapp.payloads.LoginDTO;
import alessioceccarini.tgcapp.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@Autowired
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping("/login")
	public LoginAccessDTO login(@RequestBody LoginDTO loginDTO) {

		return new LoginAccessDTO(this.authenticationService.checkCredentialsAndReturnToken(loginDTO));
	}
}
