package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.Card;
import alessioceccarini.tgcapp.repositories.CardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

	private final CardRepo cardRepo;

	@Autowired
	public CardService(CardRepo cardRepo) {
		this.cardRepo = cardRepo;
	}

	public List<Card> searchCards(String name) {
		return cardRepo.findByCardNameContainingIgnoreCase(name);
	}

}

