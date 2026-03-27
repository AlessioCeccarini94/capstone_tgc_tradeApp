package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.Card;
import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.entities.UserCardsList;
import alessioceccarini.tgcapp.entities.UserFavCards;
import alessioceccarini.tgcapp.enums.Condition;
import alessioceccarini.tgcapp.exceptions.NotFoundException;
import alessioceccarini.tgcapp.exceptions.UnauthorizedException;
import alessioceccarini.tgcapp.payloads.CardOwnerDTO;
import alessioceccarini.tgcapp.repositories.CardRepo;
import alessioceccarini.tgcapp.repositories.UserCardListRepo;
import alessioceccarini.tgcapp.repositories.UserFavCardsRepo;
import alessioceccarini.tgcapp.specifications.CardSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {

	private final CardRepo cardRepo;
	private final UserCardListRepo userCardListRepo;
	private final UserService userService;
	private final UserFavCardsRepo userFavCardsRepo;

	@Autowired
	public CardService(CardRepo cardRepo, UserCardListRepo userCardListRepo, UserService userService, UserFavCardsRepo userFavCardsRepo) {
		this.cardRepo = cardRepo;
		this.userCardListRepo = userCardListRepo;
		this.userService = userService;
		this.userFavCardsRepo = userFavCardsRepo;
	}

	//---------------------------------------------------- G E T -------------------------------------------------------

	public List<Card> findAll() {
		return cardRepo.findAll();
	}

	public Page<Card> searchCards(String name, Long gameId, Pageable pageable) {
		if (gameId != null && name != null && !name.isBlank()) {
			return cardRepo.findByExpansion_Game_IdAndCardNameContainingIgnoreCase(gameId, name, pageable);
		}
		if (gameId != null) {
			return cardRepo.findByExpansion_Game_Id(gameId, pageable);
		}
		if (name != null && !name.isBlank()) {
			return cardRepo.findByCardNameContainingIgnoreCase(name, pageable);
		}
		return cardRepo.findAll(pageable);
	}

	public Page<Card> filterCards(
			Long gameId,
			Long expansionId,
			Double minPrice,
			Double maxPrice,
			Pageable pageable
	) {
		Specification<Card> spec = Specification.where((root, query, criteriaBuilder) ->
				criteriaBuilder.conjunction());
		if (gameId != null) {
			spec = spec.and(CardSpecification.hasGame(gameId));
		}
		if (expansionId != null) {
			spec = spec.and(CardSpecification.hasExpansion(expansionId));
		}
		if (minPrice != null) {
			spec = spec.and(CardSpecification.priceGreaterThan(minPrice));
		}
		if (maxPrice != null) {
			spec = spec.and(CardSpecification.priceLessThan(maxPrice));
		}

		return cardRepo.findAll(spec, pageable);
	}

	public Page<Card> findAllById(Long id, int page, int size) {
		if (size <= 0 || size > 100) size = 12;
		Pageable pageable = PageRequest.of(page - 1, size);
		return cardRepo.findByExpansion_Game_Id(id, pageable);
	}

	public Card findById(Long id) {
		return cardRepo.findByBlueprintId(id);
	}

	public Page<Card> findByExpansionId(Long expansionId, int page, int size) {
		if (size <= 0 || size > 500) size = 200;
		if (page < 0) page = 0;
		Pageable pageable = PageRequest.of(page, size);
		return cardRepo.findByExpansionCardTraderId(expansionId, pageable);
	}

	public Page<Card> orderByPrice(int size) {
		return cardRepo.findAllByAvgPrice(PageRequest.of(0, size));
	}

	public List<CardOwnerDTO> findOwnerByCardId(Long blueprintId) {
		List<UserCardsList> owners = userCardListRepo.findByCard_BlueprintId(blueprintId);

		return owners.stream()
				.map(card -> new CardOwnerDTO(
						card.getUser().getUserId(),
						card.getUser().getUsername(),
						card.getUser().getRating(),
						card.getUser().getEmail()
				)).toList();
	}

	public List<UserCardsList> findUserCollection(UUID userId) {
		return userCardListRepo.findByUser_UserId(userId);
	}


	public List<UserFavCards> findUserFavCards(UUID userId) {
		return userFavCardsRepo.findByUser_UserId(userId);
	}

	//--------------------------------------------------- P O S T ------------------------------------------------------

	public UserCardsList addCard(UUID userId, Long cardId) {

		Optional<UserCardsList> existingCard = userCardListRepo.findByUser_UserIdAndCard_BlueprintId(userId, cardId);
		if (existingCard.isPresent()) {
			UserCardsList userCardsList = existingCard.get();
			userCardsList.setQuantity(userCardsList.getQuantity() + 1);

			return userCardListRepo.save(userCardsList);
		}
		User user = userService.findById(userId);
		Card card = cardRepo.findByBlueprintId(cardId);

		UserCardsList newCard = new UserCardsList();
		newCard.setUser(user);
		newCard.setCard(card);
		newCard.setQuantity(1);
		newCard.setCondition(Condition.NEAR_MINT);
		newCard.setForTrade(false);

		return userCardListRepo.save(newCard);
	}

	//----------------------------------------------- FAVORITES CARDS --------------------------------------------------

	public UserFavCards addFavCard(UUID userId, Long cardId) {
		Optional<UserFavCards> existingCard = userFavCardsRepo.findByUser_UserIdAndCard_BlueprintId(userId, cardId);
		if (existingCard.isPresent()) {
			UserFavCards userFavCards = existingCard.get();

			return userFavCardsRepo.save(userFavCards);
		}
		User user = userService.findById(userId);
		Card card = cardRepo.findByBlueprintId(cardId);

		UserFavCards newCard = new UserFavCards();
		newCard.setUser(user);
		newCard.setCard(card);

		return userFavCardsRepo.save(newCard);
	}

	//-------------------------------------------------- G E T ---------------------------------------------------------
	public List<UserCardsList> findAllUserCardsList(UUID userId) {
		return userCardListRepo.findByUser_UserId(userId);
	}

	//--------------------------------------------- FAVORITES CARDS ----------------------------------------------------

	public List<UserFavCards> findAllUserFavCardsList(UUID userId) {
		return userFavCardsRepo.findByUser_UserId(userId);
	}

	//-------------------------------------------------- P U T ---------------------------------------------------------

	public UserCardsList updateCard(UUID userId, UUID cardId, Condition condition) {
		UserCardsList cardsList = userCardListRepo.findByUuid(cardId)
				.orElseThrow(() -> new NotFoundException("Card not found"));
		if (!cardsList.getUser().getUserId().equals(userId)) {
			throw new UnauthorizedException("Not your card");
		}
		cardsList.setCondition(condition);
		return userCardListRepo.save(cardsList);
	}


	//------------------------------------------------ D E L E T E -----------------------------------------------------

	public void deleteCard(UUID userId, Long cardId) {
		UserCardsList card = userCardListRepo.findByUser_UserIdAndCard_BlueprintId(userId, cardId).orElseThrow(() -> new NotFoundException("Card not found"));
		userCardListRepo.delete(card);
	}

	//---------------------------------------------- FAVORITES CARDS ---------------------------------------------------

	public void deleteFavCard(UUID userId, Long cardId) {
		UserFavCards cards = userFavCardsRepo.findByUser_UserIdAndCard_BlueprintId(userId, cardId).orElseThrow(() -> new NotFoundException("Card not found"));
		userFavCardsRepo.delete(cards);
	}
}

