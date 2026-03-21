package alessioceccarini.tgcapp.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "provinces")
@Getter
@Setter
@ToString
public class Province {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Setter(AccessLevel.NONE)
	private UUID id;
	@Column(nullable = false)
	private String shortName;
	@Column(nullable = false)
	private String province;
	@Column(nullable = false)
	private String region;

	public Province() {
	}

	public Province(String shortName, String province, String region) {
		this.shortName = shortName;
		this.province = province;
		this.region = region;
	}
}
