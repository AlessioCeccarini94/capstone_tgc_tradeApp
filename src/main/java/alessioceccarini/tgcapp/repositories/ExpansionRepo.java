package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.Expansion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ExpansionRepo extends JpaRepository<Expansion, Long>, JpaSpecificationExecutor<Expansion> {
	List<Expansion> findAll();

	Optional<Expansion> findByCardTraderId(Long id);
}
