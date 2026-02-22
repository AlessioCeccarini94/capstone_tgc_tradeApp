package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	//------------------------------------------ G E T --------------------------------------------------

	@GetMapping
	public Page<User> findAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "username") String orderBy) {
		return userService.findAll(page, size, orderBy);
	}

	@GetMapping("/{userId}")
	public User findById(@PathVariable UUID id) {
		return userService.findById(id);
	}

//	@PostMapping
//	@ResponseStatus(HttpStatus.CREATED)
//	public User save(@RequestBody @Validated UserDTO userDTO) {
//		return userService.save(userDTO);
//	}
}
