package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.payloads.LoginAccessDTO;
import alessioceccarini.tgcapp.payloads.LoginDTO;
import alessioceccarini.tgcapp.payloads.UserDTO;
import alessioceccarini.tgcapp.services.AuthenticationService;
import alessioceccarini.tgcapp.services.EmailSender;
import alessioceccarini.tgcapp.services.UserService;
import com.mashape.unirest.http.exceptions.UnirestException;
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
	private final EmailSender emailSender;

	@Autowired
	public AuthenticationController(AuthenticationService authenticationService, UserService userService, EmailSender emailSender) {
		this.authenticationService = authenticationService;
		this.userService = userService;
		this.emailSender = emailSender;
	}

	@PostMapping("/login")
	public LoginAccessDTO login(@RequestBody LoginDTO loginDTO) {

		return new LoginAccessDTO(this.authenticationService.checkCredentialsAndReturnToken(loginDTO));
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User save(@RequestBody @Validated UserDTO userDTO, BindingResult bindingResult) throws UnirestException {

		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
			throw new ValidationException(errors.toString());
		} else {
			User savedUser = this.userService.save(userDTO);
			emailSender.sendWelcomeEmail(userDTO);
			return savedUser;

		}
	}
}
