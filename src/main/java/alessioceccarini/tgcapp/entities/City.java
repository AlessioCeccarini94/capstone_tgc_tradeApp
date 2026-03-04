package alessioceccarini.tgcapp.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "city")
@Getter
@Setter
@ToString
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Setter(AccessLevel.NONE)
	private UUID id;
	@Column(nullable = false)
	private String cityName;
	@ManyToOne
	@JoinColumn(name = "province_id", nullable = false)
	private Province province;

	public City() {
	}

	public City(String cityName, Province province) {
		this.cityName = cityName;
		this.province = province;
	}
}