package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.user_entities.UserCardsList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCardListRepo extends JpaRepository<UserCardsList, Long> {

	Optional<UserCardsList> findByUser_UserIdAndCard_BlueprintId(UUID userId, Long cardId);

}
