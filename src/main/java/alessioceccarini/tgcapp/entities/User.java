package alessioceccarini.tgcapp.entities;


import alessioceccarini.tgcapp.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString(exclude = "city")
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID userId;
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
	@Column(nullable = false)
	private double rating = 0;
	private String image;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id", nullable = false)
	private City city;


	public User(String firstName, String lastName, String username, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = Role.USER;
		this.rating = 0.0;
		this.image = "https://ui-avatars.com/api/?name=" + firstName + lastName;

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
	}

}
