package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.entities.user_entities.User;
import alessioceccarini.tgcapp.payloads.UserResponseDTO;
import alessioceccarini.tgcapp.payloads.UserUpdateDTO;
import alessioceccarini.tgcapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	//----------------------------------------- P O S T  ------------------------------------------------

	//------------------------------------------ G E T --------------------------------------------------

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<User> findAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "username") String orderBy) {
		return userService.findAllUsers(page, size, orderBy);
	}

	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public UserResponseDTO getUser(@PathVariable UUID userId) {
		User user = userService.findById(userId);
		return new UserResponseDTO(
				user.getUserId(),
				user.getFirstName(),
				user.getLastName(),
				user.getUsername(),
				user.getEmail(),
				user.getCity().getCityName(),
				user.getRating(),
				user.getImage()
		);
	}

	@GetMapping("/me")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public UserResponseDTO getCurrentUser(@AuthenticationPrincipal User principal) {
		User user = userService.findById(principal.getUserId());

		return new UserResponseDTO(
				user.getUserId(),
				user.getFirstName(),
				user.getLastName(),
				user.getUsername(),
				user.getEmail(),
				user.getCity().getCityName(),
				user.getRating(),
				user.getImage()
		);
	}
	//-------------------------------------- F I L T E R ------------------------------------------------

	//------------------------------------------ P U T --------------------------------------------------
	@PutMapping("/{userId}")
	public UserResponseDTO update(@PathVariable UUID userId,
								  @RequestBody @Validated UserUpdateDTO userUpdateDTO) {
		User user = userService.updateUser(userId, userUpdateDTO);

		return new UserResponseDTO(
				user.getUserId(),
				user.getFirstName(),
				user.getLastName(),
				user.getUsername(),
				user.getEmail(),
				user.getCity().getCityName(),
				user.getRating(),
				user.getImage()
		);

	}

	//--------------------------------------- P A T C H -------------------------------------------------
	@PatchMapping("/{userId}/image")
	@PreAuthorize("hasAnyRole('UTENTE','ADMIN')")
	public User updateImage(@PathVariable UUID userId, @RequestParam("image") MultipartFile image) {
		try {
			return userService.updateProfileImg(userId, image);
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

	//--------------------------------------  D E L E T E -----------------------------------------------

	@DeleteMapping("/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		userService.deleteUser(id);
	}

}
