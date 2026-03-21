package alessioceccarini.tgcapp.entities;


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
	@Column(unique = true)
	private Long blueprintId;
	@Column(length = 500)
	private String cardName;
	@Column(columnDefinition = "TEXT")
	private String image;
	@ManyToOne
	@JoinColumn(name = "expansion_id")
	private Expansion expansion;
	@Column(name = "avg_price")
	private Double avgPrice;


	public Card(String cardName, Expansion expansion) {
		this.cardName = cardName;
		this.expansion = expansion;
		this.image = "https://ui-avatars.com/api/?name=" + cardName;
	}
}
