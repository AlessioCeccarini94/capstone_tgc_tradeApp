package alessioceccarini.tgcapp.entities;

import alessioceccarini.tgcapp.enums.MessageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "message")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Column
	private String message;
	@ManyToOne(fetch = FetchType.LAZY)
	private User sender;
	@ManyToOne(fetch = FetchType.LAZY)
	private User receiver;
	@Column
	private MessageType type;
	@Column
	private LocalDate timestamp;
}
