package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.user_entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

	Page<User> findAll(Pageable pageable);

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}

