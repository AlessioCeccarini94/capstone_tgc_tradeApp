package alessioceccarini.tgcapp.entities;


import alessioceccarini.tgcapp.enums.Rarity;
import alessioceccarini.tgcapp.enums.TgcType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID uuid;
	@Column(nullable = false)
	private String cardName;
	@Column(nullable = false)
	private TgcType tgcType;
	@Column(nullable = false)
	private Rarity rarity;
	@Column(nullable = false)
	private String expansion;
	private String image;
	private double avgPrice;

	public Card(String cardName, TgcType tgcType, Rarity rarity, String expansion) {
		this.cardName = cardName;
		this.tgcType = tgcType;
		this.rarity = rarity;
		this.expansion = expansion;
		this.avgPrice = 0.0;
		this.image = "https://ui-avatars.com/api/?name=" + cardName;
	}
}
