package alessioceccarini.tgcapp.security;

import alessioceccarini.tgcapp.entities.User;
import alessioceccarini.tgcapp.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {

	@Value("${jwt.secret}")
	private String jwtSecret;

	public String genearateToken(User user) {

		return Jwts.builder().issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
				.subject(String.valueOf(user.getUuid()))
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

}
