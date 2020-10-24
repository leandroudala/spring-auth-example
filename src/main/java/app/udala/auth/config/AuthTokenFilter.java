package app.udala.auth.config;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import app.udala.auth.model.User;
import app.udala.auth.repository.UserRepository;
import app.udala.auth.service.TokenService;

public class AuthTokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UserRepository repository;

	public AuthTokenFilter(TokenService tokenService, UserRepository userRepository) {
		super();
		this.tokenService = tokenService;
		this.repository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = getToken(request);
		boolean valid = tokenService.isValidToken(token);
		if (valid) {
			authUser(token);
		}

		filterChain.doFilter(request, response);
	}

	private void authUser(String token) {
		UUID publicId = tokenService.getUserPublicId(token);
		Optional<User> user = repository.findByPublicId(publicId);

		if (user.isPresent()) {
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.get(), null,
					user.get().getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}

	}

	// Retrieve token
	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer "))
			return null;
		return token.substring(7, token.length());
	}

}
