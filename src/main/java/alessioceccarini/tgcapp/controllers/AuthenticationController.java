package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.payloads.LoginAccessDTO;
import alessioceccarini.tgcapp.payloads.LoginDTO;
import alessioceccarini.tgcapp.payloads.UserDTO;
import alessioceccarini.tgcapp.services.AuthenticationService;
import alessioceccarini.tgcapp.services.UserService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final UserService userService;

	@Autowired
	public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
		this.authenticationService = authenticationService;
		this.userService = userService;
	}

	@PostMapping("/login")
	public LoginAccessDTO login(@RequestBody LoginDTO loginDTO) {

		return new LoginAccessDTO(this.authenticationService.checkCredentialsAndReturnToken(loginDTO));
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User save(@RequestBody @Validated UserDTO userDTO, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
			throw new ValidationException(errors.toString());
		} else {
			return this.userService.save(userDTO);

		}
	}
}
