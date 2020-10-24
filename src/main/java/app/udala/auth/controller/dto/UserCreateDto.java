package app.udala.auth.controller.dto;

import java.util.UUID;

import app.udala.auth.model.User;

public class UserCreateDto {

	private UUID publicId;
	private String email;
	private String password;
	private String username;
	private String name;

	public UserCreateDto() {
	}

	public UserCreateDto(User user) {
		this.email = user.getEmail();
		this.password = user.getPassword_hash();
		this.username = user.getUsername();
		this.name = user.getName();
	}

	public UUID getPublicId() {
		return publicId;
	}

	public void setPublicId(UUID publicId) {
		this.publicId = publicId;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User convert() {
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword_hash(password);
		user.setName(name);

		return user;
	}
}
