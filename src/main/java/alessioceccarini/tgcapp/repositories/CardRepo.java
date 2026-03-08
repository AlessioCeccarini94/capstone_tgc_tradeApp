package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CardRepo extends JpaRepository<Card, UUID>, JpaSpecificationExecutor<Card> {

	List<Card> findByCardNameContainingIgnoreCase(String name);

	boolean existsByBlueprintId(Long blueprintId);

	@Query("SELECT c.blueprintId FROM Card c")
	List<Long> findAllBlueprintIds();
}
