package alessioceccarini.tgcapp.entities;

import alessioceccarini.tgcapp.entities.user_entities.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Trade {
	@ManyToOne
	User sender;
	@ManyToOne
	User receiver;
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String ststus;
}
