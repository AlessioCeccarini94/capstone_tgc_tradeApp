package alessioceccarini.tgcapp.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class TradeItem {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@ManyToOne
	private Trade trade;
	@ManyToOne
	private Card card;
	private int quantity;
}
