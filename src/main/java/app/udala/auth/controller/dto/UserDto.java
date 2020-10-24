package app.udala.auth.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;

import app.udala.auth.model.User;

public class UserDto {
    private UUID publicID;
    private String username;
    private String name;
    private LocalDateTime registeredOn;

    public String getUsername() {
        return username;
    }

    public UUID getPublicID() {
        return publicID;
    }

    public void setPublicID(UUID publicID) {
        this.publicID = publicID;
    }

    public LocalDateTime getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDateTime registeredOn) {
        this.registeredOn = registeredOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserDto(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.registeredOn = user.getRegisteredOn();
        this.publicID = user.getPublicId();
    }

    public static Page<UserDto> convert(Page<User> users) {
        return users.map(UserDto::new);
    }

}
