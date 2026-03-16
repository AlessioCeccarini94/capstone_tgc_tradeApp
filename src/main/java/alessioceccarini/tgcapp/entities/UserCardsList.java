package alessioceccarini.tgcapp.entities;


import alessioceccarini.tgcapp.enums.Condition;
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
@Table(name = "user_cards_list")
public class UserCardsList {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID uuid;
	@ManyToOne
	private User user;
	@ManyToOne
	private Card card;
	@Enumerated(EnumType.STRING)
	private Condition condition;
	private int quantity;
	private boolean forTrade;
}
