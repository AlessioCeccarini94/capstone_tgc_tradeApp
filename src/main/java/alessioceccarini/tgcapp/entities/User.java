package alessioceccarini.tgcapp.entities;


import alessioceccarini.tgcapp.enums.Role;
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
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID uuid;
	@Column(nullable = false)  // TODO: Inserire nel token
	private String firstName;
	@Column(nullable = false)
	private String lastName;
	@Column(unique = true)
	private String username;
	@Column(unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;         // TODO: Inserire nel token
	private double rating;
	private String image;

	public User(String firstName, String lastName, String username, String email, Role role) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.role = role;
		this.rating = 0.0;
		this.image = "https://ui-avatars.com/api/?name=" + firstName + lastName;
	}
}
