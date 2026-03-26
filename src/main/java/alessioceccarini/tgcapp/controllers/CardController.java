package alessioceccarini.tgcapp.controllers;


import alessioceccarini.tgcapp.entities.Card;
import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.entities.UserCardsList;
import alessioceccarini.tgcapp.entities.UserFavCards;
import alessioceccarini.tgcapp.exceptions.NotFoundException;
import alessioceccarini.tgcapp.payloads.CardOwnerDTO;
import alessioceccarini.tgcapp.services.CardService;
import alessioceccarini.tgcapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

	@PostMapping("/favorites/{cardId}")
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public UserFavCards createUserFavCardList(
			@PathVariable Long cardId,
			Authentication authentication) {
		if (authentication == null) {
			throw new NotFoundException("Authentication is required");
		}
		User user = (User) authentication.getPrincipal();
		return cardService.addFavCard(user.getUserId(), cardId);
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

	@GetMapping("/favorites")
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	public List<UserFavCards> findAllUserFavCardsList(@AuthenticationPrincipal User user) {
		return cardService.findAllUserFavCardsList(user.getUserId());
	}

	@GetMapping("/search")
	public Page<Card> searchCard(
			@RequestParam(required = false) String query,
			@RequestParam(required = false) Long gameId,
			@RequestParam(defaultValue = "100") int size
	) {
		Pageable pageable = PageRequest.of(0, size);
		return cardService.searchCards(query, gameId, pageable);
	}

	@GetMapping("/expansions/{expansionId}")
	public Page<Card> findByExpansionId(
			@PathVariable Long expansionId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "200") int size) {
		return cardService.findByExpansionId(expansionId, page, size);
	}

	@GetMapping("/top")
	public Page<Card> findTopCards(@RequestParam(defaultValue = "200") int size) {
		return cardService.orderByPrice(size);
	}

	@GetMapping("/{blueprintId}/owners")
	public List<CardOwnerDTO> getCardOwners(@PathVariable Long blueprintId) {
		return cardService.findOwnerByCardId(blueprintId);
	}

	@GetMapping("/collection/user/{userId}")
	public List<UserCardsList> getUserCollection(@PathVariable UUID userId) {
		return cardService.findUserCollection(userId);
	}

	@GetMapping("/favorites/user/{userId}")
	public List<UserFavCards> getUserFavCards(@PathVariable UUID userId) {
		return cardService.findUserFavCards(userId);
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

	@DeleteMapping("/favorites/{cardId}")
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFavCard(@PathVariable Long cardId, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		cardService.deleteFavCard(user.getUserId(), cardId);
	}
}

