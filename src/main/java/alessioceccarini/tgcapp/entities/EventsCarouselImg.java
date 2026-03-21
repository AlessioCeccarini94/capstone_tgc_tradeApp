package alessioceccarini.tgcapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EventsCarouselImg {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String image;
}
