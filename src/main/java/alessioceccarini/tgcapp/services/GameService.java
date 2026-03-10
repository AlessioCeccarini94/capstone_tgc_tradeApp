package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.Game;
import alessioceccarini.tgcapp.repositories.GameRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
	private final GameRepo gameRepo;

	@Autowired
	public GameService(GameRepo gameRepo) {
		this.gameRepo = gameRepo;
	}

	public List<Game> findAll() {
		return gameRepo.findAll();
	}

}
