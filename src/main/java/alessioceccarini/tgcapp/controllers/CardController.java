package alessioceccarini.tgcapp.controllers;


import alessioceccarini.tgcapp.entities.Card;
import alessioceccarini.tgcapp.entities.user_entities.User;
import alessioceccarini.tgcapp.entities.user_entities.UserCardsList;
import alessioceccarini.tgcapp.exceptions.NotFoundException;
import alessioceccarini.tgcapp.services.CardService;
import alessioceccarini.tgcapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

	private final CardService cardService;
	private final UserService userService;

	@Autowired
	public CardController(CardService cardService, UserService userService) {
		this.cardService = cardService;
		this.userService = userService;
	}

	//---------------------------------- P O S T ---------------------------------------

	@PostMapping("/collection/{cardId}")
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	public UserCardsList createUserCardList(
			@PathVariable Long cardId,
			Authentication authentication) {
		if (authentication == null) {
			throw new NotFoundException("Authentication is required");
		}
		User user = (User) authentication.getPrincipal();
		return cardService.addCard(user.getUserId(), cardId);
	}

	//----------------------------------- G E T ----------------------------------------

	@GetMapping
	public List<Card> findAll() {
		return cardService.findAll();
	}

	@GetMapping("/{cardId}")
	public Card findById(@PathVariable Long cardId) {
		return cardService.findById(cardId);
	}

	@GetMapping("/collection")
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	public List<UserCardsList> findAllUserCardsList(@AuthenticationPrincipal User user) {

		return cardService.findAllUserCardsList(user.getUserId());
	}

	@GetMapping("/search")
	public List<Card> findByBlueprintId(@RequestParam String name) {
		return cardService.searchCards(name);
	}

	@GetMapping("/expansions/{expansionId}")
	public Page<Card> findByExpansionId(
			@PathVariable Long expansionId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "200") int size) {
		return cardService.findByExpansionId(expansionId, page, size);
	}

	@GetMapping("/top")
	public Page<Card> findTopCards(@RequestParam(defaultValue = "50") int size) {
		return cardService.orderByPrice();
	}
	
	//----------------------------------- P U T ----------------------------------------

	//-------------------------------- D E L E T E -------------------------------------

	@DeleteMapping("/collection/{cardId}")
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Long cardId, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		cardService.deleteCard(user.getUserId(), cardId);

	}
}

