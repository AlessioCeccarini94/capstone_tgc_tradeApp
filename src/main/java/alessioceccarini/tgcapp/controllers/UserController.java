package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.payloads.UserDTO;
import alessioceccarini.tgcapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

//	@PostMapping
//	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
//	@ResponseStatus(HttpStatus.CREATED)
//	public User save(@RequestBody @Validated UserDTO userDTO) {
//		return userService.save(userDTO);
//	}

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
	public User findById(@PathVariable UUID id) {
		return userService.findById(id);
	}

	//-------------------------------------- F I L T E R ------------------------------------------------

	//------------------------------------------ P U T --------------------------------------------------
	@PutMapping("/{userId}")
	public User update(@PathVariable("userId") UUID id,
					   @RequestBody @Validated UserDTO userDTO) {
		return userService.updateUser(id, userDTO);
	}

	//--------------------------------------- P A T C H -------------------------------------------------
	@PatchMapping("/{userId}/image")
	@PreAuthorize("hasAnyAuthority('UTENTE','ADMIN')")
	public User updateImage(@PathVariable UUID id, @RequestParam("image") MultipartFile image) {
		try {
			return userService.updateProfileImg(id, image);
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
