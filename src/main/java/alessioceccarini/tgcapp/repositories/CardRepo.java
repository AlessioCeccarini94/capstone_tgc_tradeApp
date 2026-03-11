package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CardRepo extends JpaRepository<Card, UUID>, JpaSpecificationExecutor<Card> {

	List<Card> findByCardNameContainingIgnoreCase(String name);

	List<Card> findAll();

	@Query("SELECT c.blueprintId FROM Card c")
	List<Long> findAllBlueprintIds();

	Page<Card> findByExpansion_Game_Id(Long id, Pageable pageable);

	Card findByBlueprintId(Long id);

	Page<Card> findByExpansionCardTraderId(Long id, Pageable pageable);

}
