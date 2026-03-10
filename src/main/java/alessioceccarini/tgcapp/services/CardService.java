package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.Card;
import alessioceccarini.tgcapp.entities.user_entities.User;
import alessioceccarini.tgcapp.entities.user_entities.UserCardsList;
import alessioceccarini.tgcapp.enums.Condition;
import alessioceccarini.tgcapp.exceptions.NotFoundException;
import alessioceccarini.tgcapp.repositories.CardRepo;
import alessioceccarini.tgcapp.repositories.UserCardListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {

	private final CardRepo cardRepo;
	private final UserCardListRepo userCardListRepo;
	private final UserService userService;

	@Autowired
	public CardService(CardRepo cardRepo, UserCardListRepo userCardListRepo, UserService userService) {
		this.cardRepo = cardRepo;
		this.userCardListRepo = userCardListRepo;
		this.userService = userService;
	}

	//------------------------------------------ G E T -----------------------------------------------------

	public List<Card> findAll() {
		return cardRepo.findAll();
	}

	public List<Card> searchCards(String name) {
		return cardRepo.findByCardNameContainingIgnoreCase(name);
	}

	public Page<Card> findAllById(Long id, int page, int size) {
		if (size <= 0 || size > 100) size = 12;
		Pageable pageable = PageRequest.of(page - 1, size);
		return cardRepo.findByExpansion_Game_Id(id, pageable);
	}

	public Card findById(Long id) {
		return cardRepo.findByBlueprintId(id);
	}


	//------------------------------------- USER CARD LIST ----------------------------------------------

	//---------------------------------------- P O S T ---------------------------------------------------

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

	//------------------------------------------ G E T -----------------------------------------------------
	public List<UserCardsList> findAllUserCardsList(UUID userId) {
		return userCardListRepo.findAll();
	}
	//---------------------------------------- D E L E T E --------------------------------------------------

	public void deleteCard(UUID userId, Long cardId) {
		UserCardsList card = userCardListRepo.findByUser_UserIdAndCard_BlueprintId(userId, cardId).orElseThrow(() -> new NotFoundException("Card not found"));
		userCardListRepo.delete(card);
	}
}

