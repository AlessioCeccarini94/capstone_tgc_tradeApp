package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.UserCardsList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserCardListRepo extends JpaRepository<UserCardsList, UUID> {

	Optional<UserCardsList> findByUser_UserIdAndCard_BlueprintId(UUID userId, Long cardId);

	List<UserCardsList> findByCard_BlueprintId(Long cardId);

	List<UserCardsList> findByUser_UserId(UUID userId);

	Optional<UserCardsList> findByUuid(UUID uuid);
}
