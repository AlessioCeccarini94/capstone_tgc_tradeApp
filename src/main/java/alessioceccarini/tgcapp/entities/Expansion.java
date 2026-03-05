package alessioceccarini.tgcapp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "expansions")
@Getter
@Setter
@NoArgsConstructor
public class Expansion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private Long cardTraderId;
	private String name;
	@ManyToOne
	private Game game;
}
