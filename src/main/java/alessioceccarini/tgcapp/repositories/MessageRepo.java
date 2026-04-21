package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.Message;
import alessioceccarini.tgcapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepo extends JpaRepository<Message, UUID> {
	@Query("SELECT m FROM Message m WHERE " +
			"(m.sender = :user1 AND m.receiver = :user2) OR " +
			"(m.sender = :user2 AND m.receiver = :user1) " +
			"ORDER BY m.timestamp ASC")
	List<Message> findConversation(@Param("user1") User user1,
								   @Param("user2") User user2);
}
