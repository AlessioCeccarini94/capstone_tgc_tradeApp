package alessioceccarini.tgcapp.security;

import alessioceccarini.tgcapp.entities.user_entities.User;
import alessioceccarini.tgcapp.exceptions.UnauthorizedException;
import alessioceccarini.tgcapp.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {

	private final JWTTools jwtTools;
	private final UserService userService;

	@Autowired
	public JWTFilter(JWTTools jwtTools, UserService userService) {
		this.jwtTools = jwtTools;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		//        ------------------------ AUTENTICAZIONE ------------------------

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer "))
			throw new UnauthorizedException("Unauthorized");
		String accessToken = authHeader.replace("Bearer ", "");
		jwtTools.verifyToken(accessToken);

		//        ------------------------ AUTORIZZAZIONE ------------------------

		UUID userId = jwtTools.extractIdFromToken(accessToken);
		User user = this.userService.findById(userId);
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);

	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if ("OPTIONS".equals(request.getMethod())) {
			return true;
		}
		AntPathMatcher matcher = new AntPathMatcher();
		return matcher.match("/auth/**", request.getRequestURI()) ||
				matcher.match("/cities/**", request.getRequestURI());
	}
}
