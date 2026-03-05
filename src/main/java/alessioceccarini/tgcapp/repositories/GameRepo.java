package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GameRepo extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

}
