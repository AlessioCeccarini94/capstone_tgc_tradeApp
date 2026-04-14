package alessioceccarini.tgcapp.tools;

import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {

	@Value("${jwt.secret}")
	private String jwtSecret;

	public String generateToken(User user) {
		return Jwts.builder()
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
				.subject(user.getUsername())
				.claim("userId", user.getUserId())
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
				.compact();
	}

	public void verifyToken(String token) {
		try {
			Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build().parse(token);
		} catch (Exception e) {
			throw new UnauthorizedException("Invalid token");
		}
	}

	public UUID extractIdFromToken(String token) {
		return UUID.fromString(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("userId", String.class));
	}

	public String extractUsername(String token) {
		return Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

}
