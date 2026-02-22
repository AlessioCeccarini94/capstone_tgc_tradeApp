package alessioceccarini.tgcapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

		// Disattivare login
		httpSecurity.formLogin(form -> form.disable());

		// Disattivare sicurezza attacchi CSFR
		httpSecurity.csrf(csrf -> csrf.disable());

		// impostare lavorazione STATELESS
		httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Permettere autenticazione a tutte le ricjieste HTTP
		httpSecurity.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/**").permitAll());

		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

}
