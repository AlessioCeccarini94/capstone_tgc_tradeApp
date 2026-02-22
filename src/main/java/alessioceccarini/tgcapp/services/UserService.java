package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.exceptions.NotFoundException;
import alessioceccarini.tgcapp.payloads.UserDTO;
import alessioceccarini.tgcapp.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	//----------------------------------------- P O S T -------------------------------------------------

	public User save(UserDTO userDTO) {
		this.userRepo.findByEmail(userDTO.email()).ifPresent(user -> {
			throw new RuntimeException("Email already exists");
		});
		User newUser = new User(
				userDTO.firstName(),
				userDTO.lastName(),
				userDTO.username(),
				userDTO.email(),
				passwordEncoder.encode(userDTO.password()),
				userDTO.role()
		);
		return this.userRepo.save(newUser);
	}

	//------------------------------------------ P U T --------------------------------------------------

	//---------------------------------------- P A T C H ------------------------------------------------

	//--------------------------------------  D E L E T E -----------------------------------------------

	//------------------------------------------ G E T --------------------------------------------------

	public Page<User> findAll(int page, int size, String sortBy) {
		if (page < 0) page = 0;
		if (size < 0 || size > 150) size = 10;
		if (sortBy == null) sortBy = "name";
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
		return userRepo.findAll(pageable);
	}

	public User findById(UUID id) {
		return userRepo.findById(id).orElseThrow(() -> new NotFoundException("not found"));
	}

	public User findByUsername(String username) {
		return userRepo.findByUsername(username).orElseThrow(() -> new NotFoundException("not found"));
	}

	public User findByEmail(String email) {
		return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("not found"));
	}

}
