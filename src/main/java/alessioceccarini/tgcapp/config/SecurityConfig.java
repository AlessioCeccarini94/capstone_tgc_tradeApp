package alessioceccarini.tgcapp.config;

import alessioceccarini.tgcapp.security.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JWTFilter jwtFilter;

	@Autowired
	public SecurityConfig(JWTFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

		// Disattivare login
		httpSecurity.formLogin(form -> form.disable());

		// Disattivare sicurezza attacchi CSFR
		httpSecurity.csrf(csrf -> csrf.disable());

		// impostare lavorazione STATELESS
		httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Permettere autenticazione a tutte le ricjieste HTTP
		httpSecurity.authorizeHttpRequests(authorizeRequests -> authorizeRequests
				.requestMatchers("/auth/**").permitAll()
				.requestMatchers("/games/**").permitAll()
				.requestMatchers("/cards/top").permitAll()
				.requestMatchers("/cards").permitAll()
				.requestMatchers("/cards/search").permitAll()
				.requestMatchers("/cards/expansions/**").permitAll()
				.requestMatchers("/users/**").permitAll()
				.requestMatchers("/cities").permitAll()
				.requestMatchers("/carousels/**").permitAll()
				.anyRequest().authenticated());

		httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
