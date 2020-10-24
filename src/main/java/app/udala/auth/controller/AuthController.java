package app.udala.auth.controller;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.udala.auth.controller.dto.SignInDto;
import app.udala.auth.controller.dto.TokenDto;
import app.udala.auth.controller.dto.UserDto;
import app.udala.auth.model.User;
import app.udala.auth.repository.UserRepository;
import app.udala.auth.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	ResponseEntity<TokenDto> signIn(@RequestBody @Valid SignInDto signIn) {
		UsernamePasswordAuthenticationToken signInData = signIn.convert();

		try {
			Authentication authenticate = authManager.authenticate(signInData);
			String token = tokenService.generateToken(authenticate);

			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping
	ResponseEntity<UserDto> getData(@RequestHeader HttpHeaders headers) {
		String token = null;

		// check if authorization exists
		String authorization = headers.getFirst("Authorization");
		if (authorization != null && authorization.startsWith("Bearer")) {
			token = authorization.substring(7, authorization.length());
		}
		
		// check if token exists
		if (token != null) {
			UUID publicId = tokenService.getUserPublicId(token);
			Optional<User> userFound = userRepository.findByPublicId(publicId);
			
			// check if user has found
			if (userFound.isPresent()) {
				return ResponseEntity.ok(new UserDto(userFound.get()));
			}
		}		
		
		return ResponseEntity.badRequest().build();
	}
}
