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

import java.util.*;

@Service
public class CardImporterService {

	private static final Map<Long, String> TARGET_GAMES = Map.of(
			1L, "Magic The Gathering",
			5L, "Pokémon",
			4L, "Yu-Gi-Oh!",
			9L, "Dragon Ball Super",
			15L, "One Piece",
			18L, "Lorcana"


	);
	private final RestTemplate restTemplate;
	private final CardRepo cardRepo;
	private final ExpansionRepo expansionRepo;
	private final GameRepo gameRepo;
	private final Map<Long, Game> gameCache = new HashMap<>();
	private final Map<String, Expansion> expansionCache = new HashMap<>();
	private final Map<Long, String> expansionNames = new HashMap<>();
	@Value("${cardtrader.apikey}")
	private String apikey;

	@Autowired
	public CardImporterService(
			RestTemplate restTemplate,
			CardRepo cardRepo,
			ExpansionRepo expansionRepo,
			GameRepo gameRepo
	) {
		this.restTemplate = restTemplate;
		this.cardRepo = cardRepo;
		this.expansionRepo = expansionRepo;
		this.gameRepo = gameRepo;
	}

	// ----------------------------------------------------

	public void importCards() {

		Set<Long> existingIds = new HashSet<>(cardRepo.findAllBlueprintIds());

		List<Card> batch = new ArrayList<>();

		for (Long gameId : TARGET_GAMES.keySet()) {

			String gameName = TARGET_GAMES.get(gameId);

			System.out.println("IMPORTING GAME: " + gameName);

			Game game = getOrCreateGame(gameId, gameName);

			List<Long> expansions = getExpansionsForGame(gameId);
			System.out.println("EXPANSIONS: " + expansions.size());

			for (Long expansionId : expansions) {

				String expansionName = expansionNames.get(expansionId);

				Expansion expansion = getOrCreateExpansion(expansionId, expansionName, game);

				int page = 1;

				while (true) {

					String url =
							"https://api.cardtrader.com/api/v2/blueprints?expansion_id="
									+ expansionId +
									"&page=" + page +
									"&page_size=100";

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

					if (blueprints == null || blueprints.isEmpty()) break;

					for (Map<String, Object> blueprint : blueprints) {

						Long blueprintId = Long.valueOf(blueprint.get("id").toString());

						if (existingIds.contains(blueprintId)) continue;

						String cardName = blueprint.get("name").toString();

						Map<String, Object> imageMap =
								(Map<String, Object>) blueprint.get("image");

						String image = null;

						if (imageMap != null && imageMap.get("url") != null) {
							image = "https://api.cardtrader.com" + imageMap.get("url");
						}

						Card card = new Card();
						card.setBlueprintId(blueprintId);
						card.setCardName(cardName);
						card.setImage(image);
						card.setExpansion(expansion);

						batch.add(card);
						existingIds.add(blueprintId);

						if (batch.size() >= 1000) {
							cardRepo.saveAll(batch);
							batch.clear();
							System.out.println("Saved batch → 1000");
						}
					}

					page++;
				}
			}
		}

		if (!batch.isEmpty()) {
			cardRepo.saveAll(batch);
		}

		System.out.println("IMPORT COMPLETED");
	}

	// ---------------------------------------------------- GAME ----------------------------------------------------

	private Game getOrCreateGame(Long gameId, String name) {

		Game game = gameCache.get(gameId);

		if (game != null) return game;

		game = gameRepo.findById(gameId).orElse(null);

		if (game == null) {

			game = new Game();
			game.setId(gameId);
			game.setName(name);

			game = gameRepo.save(game);
		}

		gameCache.put(gameId, game);

		return game;
	}

	// ---------------------------------------------------- EXPANSION ----------------------------------------------------

	private Expansion getOrCreateExpansion(
			Long expansionId,
			String expansionName,
			Game game
	) {

		String key = game.getId() + "_" + expansionId;

		Expansion expansion = expansionCache.get(key);

		if (expansion != null) return expansion;

		expansion = expansionRepo
				.findByCardTraderId(expansionId)
				.orElse(null);

		if (expansion == null) {

			Expansion newExpansion = new Expansion();
			newExpansion.setCardTraderId(expansionId);
			newExpansion.setName(expansionName);
			newExpansion.setGame(game);

			expansion = expansionRepo.save(newExpansion);
		}

		expansionCache.put(key, expansion);

		return expansion;
	}


	public List<Long> getExpansionsForGame(Long gameId) {

		String url = "https://api.cardtrader.com/api/v2/expansions";

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

		List<Map<String, Object>> expansions = response.getBody();

		List<Long> expansionIds = new ArrayList<>();

		for (Map<String, Object> exp : expansions) {

			Object gameIdObj = exp.get("game_id");

			if (gameIdObj == null) continue;

			Long idGame = Long.valueOf(gameIdObj.toString());

			if (!idGame.equals(gameId)) continue;

			Long id = Long.valueOf(exp.get("id").toString());
			String name = exp.get("name").toString();

			expansionNames.put(id, name);
			expansionIds.add(id);
		}

		System.out.println("EXPANSIONS FOUND: " + expansionIds.size());

		return expansionIds;
	}
}