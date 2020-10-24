package app.udala.auth.controller;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import app.udala.auth.controller.dto.UserCreateDto;
import app.udala.auth.controller.dto.UserDto;
import app.udala.auth.model.User;
import app.udala.auth.repository.UserRepository;
import app.udala.auth.service.SecurityService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SecurityService securityService;

	@PostMapping
	ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateDto userForm, UriComponentsBuilder uriBuilder) {
		User user = userForm.convert();
		String hashedPassword = securityService.hash(user.getPassword());
		user.setPassword_hash(hashedPassword);
		userRepository.save(user);

		URI uri = uriBuilder.path("/user/{publicId}").buildAndExpand(user.getPublicId()).toUri();
		return ResponseEntity.created(uri).body(new UserDto(user));
	}

	@GetMapping
	Page<UserDto> getAll(@PageableDefault(sort = "name", page = 0, size = 10) Pageable pagination) {
		Page<User> users = userRepository.findAll(pagination);

		return UserDto.convert(users);
	}

	@GetMapping("/{publicId}")
	ResponseEntity<UserDto> getUser(@PathVariable String publicId) {
		Optional<User> userFound = userRepository.findByPublicId(UUID.fromString(publicId));
		if (userFound.isPresent()) {
			return ResponseEntity.ok(new UserDto(userFound.get()));
		}
		return ResponseEntity.notFound().build();
	}
}
