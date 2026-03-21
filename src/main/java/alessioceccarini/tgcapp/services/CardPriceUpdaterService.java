package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.Card;
import alessioceccarini.tgcapp.repositories.CardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardPriceUpdaterService {

	private CardTraderService cardTraderService;
	private CardRepo cardRepo;

	@Autowired
	public CardPriceUpdaterService(CardTraderService cardTraderService, CardRepo cardRepo) {
		this.cardTraderService = cardTraderService;
		this.cardRepo = cardRepo;
	}

	@Scheduled(fixedRate = 60000 * 60 * 6)
	public void updatePrices() {

		System.out.println("Updating card prices...");

		int page = 0;
		Page<Card> cards;

		do {

			cards = cardRepo.findAll(PageRequest.of(page, 500));
			List<Card> cardList = cards.getContent();

			for (Card card : cardList) {

				Double price = cardTraderService.getAvgPrice(card.getBlueprintId());

				System.out.println("CARD: " + card.getBlueprintId() + " PRICE: " + price);

				card.setAvgPrice(price);

				try {
					Thread.sleep(120); // evita il rate limit (max 10/sec)
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

			cardRepo.saveAll(cardList);

			page++;

		} while (cards.hasNext());

		System.out.println("Price update finished");
	}
}
