package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.Card;
import alessioceccarini.tgcapp.entities.Expansion;
import alessioceccarini.tgcapp.entities.Game;
import alessioceccarini.tgcapp.repositories.CardRepo;
import alessioceccarini.tgcapp.repositories.ExpansionRepo;
import alessioceccarini.tgcapp.repositories.GameRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CardImporterService {

	private final RestTemplate restTemplate;
	private final CardRepo cardRepo;
	private final ExpansionRepo expansionRepo;
	private final GameRepo gameRepo;
	// cache per evitare migliaia di query
	private final Map<Long, Game> gameCache = new HashMap<>();
	private final Map<Long, Expansion> expansionCache = new HashMap<>();
	@Value("${cardtrader.apikey}")
	private String apikey;


	@Autowired
	public CardImporterService(RestTemplate restTemplate,
							   CardRepo cardRepo,
							   ExpansionRepo expansionRepo,
							   GameRepo gameRepo) {
		this.restTemplate = restTemplate;
		this.cardRepo = cardRepo;
		this.expansionRepo = expansionRepo;
		this.gameRepo = gameRepo;
	}


	public void importCards() {

		List<Card> cardsBatch = new ArrayList<>();

		int page = 1;

		while (true) {

			String url = "https://api.cardtrader.com/api/v2/blueprints?page=" + page;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + apikey);

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<List<Map<String, Object>>> response =
					restTemplate.exchange(
							url,
							HttpMethod.GET,
							entity,
							new ParameterizedTypeReference<>() {
							}
					);

			List<Map<String, Object>> blueprints = response.getBody();

			if (blueprints == null || blueprints.isEmpty()) {
				break;
			}

			System.out.println("PAGE " + page + " - results " + blueprints.size());

			for (Map<String, Object> blueprint : blueprints) {


				Long blueprintId = Long.valueOf(blueprint.get("id").toString());
				if (cardRepo.existsByBlueprintId(blueprintId)) continue;
				Long gameId = Long.valueOf(blueprint.get("game_id").toString());
				if (gameId != 15) continue;
				Long expansionId = Long.valueOf(blueprint.get("expansion_id").toString());

				String name = blueprint.get("name").toString();
				String image = blueprint.get("image") != null
						? blueprint.get("image").toString()
						: null;

				List<Card> cards = new ArrayList<>();

				Game game = getOrCreateGame(gameId);
				Expansion expansion = getOrCreateExpansion(expansionId, game);


				Card card = new Card();
				card.setBlueprintId(blueprintId);
				card.setCardName(name);
				card.setImage(image);
				card.setExpansion(expansion);

				cardsBatch.add(card);
				if (cardsBatch.size() <= 100) {
					cardRepo.saveAll(cardsBatch);
					cardsBatch.clear();
				}
			}
			if (!cardsBatch.isEmpty()) {
				cardRepo.saveAll(cardsBatch);
				cardsBatch.clear();
			}

			page++;
		}
	}

	//----------------------------------------- M E T H O D S ---------------------------------------------

	private Game getOrCreateGame(Long gameId) {

		Game game = gameCache.get(gameId);

		if (game != null) return game;

		game = gameRepo.findById(gameId)
				.orElseGet(() -> {

					Game newGame = new Game();
					newGame.setId(gameId);

					return gameRepo.save(newGame);
				});

		gameCache.put(gameId, game);

		return game;
	}


	private Expansion getOrCreateExpansion(Long expansionId, Game game) {

		Expansion expansion = expansionCache.get(expansionId);

		if (expansion != null) return expansion;

		expansion = expansionRepo.findByCardTraderId(expansionId)
				.orElse(null);
		if (expansion == null) {
			Expansion newExpansion = new Expansion();
			newExpansion.setCardTraderId(expansionId);
			newExpansion.setGame(game);
			expansion = expansionRepo.saveAndFlush(newExpansion);
		}
		expansionCache.put(expansionId, expansion);

		return expansion;
	}

	//-----------------------------------------------------------------------------------------------------

}