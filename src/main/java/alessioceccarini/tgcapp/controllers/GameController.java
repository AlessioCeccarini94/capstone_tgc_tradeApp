package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.entities.Card;
import alessioceccarini.tgcapp.entities.Game;
import alessioceccarini.tgcapp.services.CardService;
import alessioceccarini.tgcapp.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

	private final GameService gameService;
	private final CardService cardService;

	@Autowired
	public GameController(GameService gameService, CardService cardService) {
		this.gameService = gameService;
		this.cardService = cardService;
	}
	//-------------------------------- G E T ------------------------------

	@GetMapping
	public List<Game> findAll() {
		return gameService.findAll();
	}

	@GetMapping({"/{gameId}/cards"})
	public Page<Card> findAllByGameId(
			@PathVariable Long gameId,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "500") int size) {

		return cardService.findAllById(gameId, page, size);
	}
}
