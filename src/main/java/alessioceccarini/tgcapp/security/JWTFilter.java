package alessioceccarini.tgcapp.security;

import alessioceccarini.tgcapp.exceptions.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

	private final JWTTools jwtTools;

	@Autowired
	public JWTFilter(JWTTools jwtTools) {
		this.jwtTools = jwtTools;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// CHECK HEADER

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer "))
			throw new UnauthorizedException("Unauthorized");

		// GET TOKEN

		String accessToken = authHeader.replace("Bearer ", "");

		// VERIFY TOKEN

		jwtTools.verifyToken(accessToken);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return new AntPathMatcher().match("/auth/**", request.getServletPath());
	}
}
