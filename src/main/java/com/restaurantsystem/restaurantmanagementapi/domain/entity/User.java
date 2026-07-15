package com.restaurantsystem.restaurantmanagementapi.domain.entity;

import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class User {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime lastModifiedDate;
    private UserType userType;
    private Address address;

    private User(
            Long id,
            String name,
            String email,
            String login,
            String password,
            LocalDateTime lastModifiedDate,
            UserType userType,
            Address address
    ) {
        validate(name, email, login, password, userType, address);
        this.id = id;
        this.name = name.trim();
        this.email = email.trim();
        this.login = login.trim();
        this.password = password;
        this.lastModifiedDate = lastModifiedDate;
        this.userType = userType;
        this.address = address;
    }

    public static User create(
            String name,
            String email,
            String login,
            String password,
            UserType userType,
            Address address
    ) {
        return new User(null, name, email, login, password, LocalDateTime.now(), userType, address);
    }

    public static User restore(
            Long id,
            String name,
            String email,
            String login,
            String password,
            LocalDateTime lastModifiedDate,
            UserType userType,
            Address address
    ) {
        return new User(id, name, email, login, password, lastModifiedDate, userType, address);
    }

    public void update(
            String name,
            String email,
            String login,
            String password,
            UserType userType,
            Address address
    ) {
        validate(name, email, login, password, userType, address);
        this.name = name.trim();
        this.email = email.trim();
        this.login = login.trim();
        this.password = password;
        this.userType = userType;
        this.address = address;
        this.lastModifiedDate = LocalDateTime.now();
    }

    private static void validate(
            String name,
            String email,
            String login,
            String password,
            UserType userType,
            Address address
    ) {
        requireText(name, "Name is required");
        requireText(email, "Email is required");
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new BusinessException("Email must be valid");
        }
        requireText(login, "Login is required");
        requireText(password, "Password is required");
        if (password.length() < 6) {
            throw new BusinessException("Password must have at least 6 characters");
        }
        if (userType == null) {
            throw new BusinessException("User type is required");
        }
        if (address == null) {
            throw new BusinessException("Address is required");
        }
    }

    private static void requireText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(message);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public UserType getUserType() {
        return userType;
    }

    public Address getAddress() {
        return address;
    }
}
