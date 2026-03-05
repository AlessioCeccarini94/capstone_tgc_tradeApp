package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CardRepo extends JpaRepository<Card, UUID>, JpaSpecificationExecutor<Card> {


	boolean existsByBlueprintId(Long blueprintId);
}
