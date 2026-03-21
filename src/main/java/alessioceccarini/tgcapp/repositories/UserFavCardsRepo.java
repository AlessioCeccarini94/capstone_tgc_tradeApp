package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.UserFavCards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserFavCardsRepo extends JpaRepository<UserFavCards, UUID> {
	
	Optional<UserFavCards> findByUser_UserIdAndCard_BlueprintId(UUID userId, Long cardId);

	List<UserFavCards> findByUser_UserId(UUID userId);

	List<UserFavCards> findByCard_BlueprintId(Long cardBlueprintId);
}
