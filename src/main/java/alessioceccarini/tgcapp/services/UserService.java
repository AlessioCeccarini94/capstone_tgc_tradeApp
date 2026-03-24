package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.City;
import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.enums.Role;
import alessioceccarini.tgcapp.exceptions.NotFoundException;
import alessioceccarini.tgcapp.payloads.UserDTO;
import alessioceccarini.tgcapp.payloads.UserUpdateDTO;
import alessioceccarini.tgcapp.repositories.CityRepo;
import alessioceccarini.tgcapp.repositories.UserRepo;
import alessioceccarini.tgcapp.specifications.UserSpecification;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final Cloudinary cloudinary;
	private final CityRepo cityRepo;


	@Autowired
	public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, Cloudinary cloudinary, CityRepo cityRepo) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.cloudinary = cloudinary;
		this.cityRepo = cityRepo;
	}

	//----------------------------------- POST ADMIN FOR RUNNER -----------------------------------------

	public User save(UserDTO userDTO) {
		this.userRepo.findByEmail(userDTO.email()).ifPresent(user -> {
			throw new RuntimeException("Email already exists");
		});
		City city = cityRepo.findById(userDTO.cityId()).orElseThrow(() -> new NotFoundException("City not found"));
		User newUser = new User(
				userDTO.firstName(),
				userDTO.lastName(),
				userDTO.username(),
				userDTO.email(),
				passwordEncoder.encode(userDTO.password())
		);
		newUser.setCity(city);
		return this.userRepo.save(newUser);
	}


	public void createAdmin(String adminFirstName, String adminLastName, String username, String email, String rawPassword) {

		if (userRepo.existsByEmail(email)) return;
		City city = cityRepo.findAll().stream().findFirst()
				.orElseThrow(() -> new NotFoundException("City not found"));

		User admin = new User();
		admin.setFirstName(adminFirstName);
		admin.setLastName(adminLastName);
		admin.setUsername(username);
		admin.setEmail(email);
		admin.setPassword(passwordEncoder.encode(rawPassword));
		admin.setRole(Role.ADMIN);
		admin.setCity(city);
		admin.setImage("https://ui-avatars.com/api/?name=" + adminFirstName + adminLastName);

		userRepo.save(admin);
	}


	//------------------------------------------ G E T --------------------------------------------------


	public Page<User> findAllUsers(int page, int size, String orderBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
		return userRepo.findAll(pageable);
	}

	public User findById(UUID id) {
		return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
	}

	public User findByUsername(String username) {
		return userRepo.findByUsername(username).orElseThrow(() -> new NotFoundException("not found"));
	}

	public User findByEmail(String email) {
		return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("not found"));
	}


	//--------------------------------------- F I L T E R -----------------------------------------------

	public Page<User> filterUsers(
			UUID cityId,
			String userrname,
			UUID provinceId) {


		Specification<User> specification = Specification.where((root, query, criteriaBuilder) -> null);
		if (cityId != null) {
			specification = specification.and(UserSpecification.hasCity(cityId));
		}
		if (userrname != null) {
			specification = specification.and(UserSpecification.hasUsername(userrname));
		}
		if (provinceId != null) {
			specification = specification.and(UserSpecification.hasProvince(provinceId));
		}

		return userRepo.findAll(specification, PageRequest.of(0, 10));
	}


	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	//------------------------------------------ P U T --------------------------------------------------

	public User updateUser(UUID id, UserUpdateDTO userUpdateDTO) {
		User user = this.findById(id);
		if (userUpdateDTO.cityId() != null) {
			City city = cityRepo.findById(userUpdateDTO.cityId()).orElseThrow(() -> new NotFoundException("City not found"));
			user.setCity(city);
		}
		user.setFirstName(userUpdateDTO.firstName());
		user.setLastName(userUpdateDTO.lastName());
		user.setUsername(userUpdateDTO.username());
		user.setEmail(userUpdateDTO.email());
		return userRepo.save(user);
	}

	//---------------------------------------- P A T C H ------------------------------------------------

	public User updateProfileImg(UUID id, MultipartFile image) throws IOException {
		User user = this.findById(id);

		String newImg = (String) cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap()).get("url");
		user.setImage(newImg);
		return userRepo.save(user);
	}

	//--------------------------------------  D E L E T E -----------------------------------------------

	public void deleteUser(UUID id) {
		User user = this.findById(id);
		userRepo.delete(user);
	}
}
