package com.restaurantsystem.restaurantmanagementapi.domain.entity;

import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainEntityTest {

    @Test
    void shouldKeepAddressAsValueObject() {
        Address first = address();
        Address second = address();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first, new Address("Outra", "10", "Centro", "Sao Paulo", "SP", "01000-000", null));
    }

    @Test
    void shouldValidateAndRenameUserType() {
        UserType userType = UserType.create("  CLIENTE  ");
        LocalDateTime previousUpdate = userType.getUpdatedAt();

        userType.rename("DONO_RESTAURANTE");

        assertEquals("DONO_RESTAURANTE", userType.getName());
        assertTrue(!userType.getUpdatedAt().isBefore(previousUpdate));
        assertThrows(BusinessException.class, () -> UserType.create(" "));
        assertThrows(BusinessException.class, () -> UserType.create("A"));
        assertThrows(BusinessException.class, () -> UserType.create("ADMIN"));
    }

    @Test
    void shouldValidateUserInvariants() {
        UserType userType = userType("CLIENTE");

        assertThrows(BusinessException.class, () -> User.create(" ", "a@b.com", "login", "123456", userType, address()));
        assertThrows(BusinessException.class, () -> User.create("Maria", "invalid", "login", "123456", userType, address()));
        assertThrows(BusinessException.class, () -> User.create("Maria", "a@b.com", " ", "123456", userType, address()));
        assertThrows(BusinessException.class, () -> User.create("Maria", "a@b.com", "login", "123", userType, address()));
        assertThrows(BusinessException.class, () -> User.create("Maria", "a@b.com", "login", "123456", null, address()));
        assertThrows(BusinessException.class, () -> User.create("Maria", "a@b.com", "login", "123456", userType, null));
    }

    @Test
    void shouldUpdateValidUser() {
        User user = User.create("Maria", "maria@email.com", "maria", "123456", userType("CLIENTE"), address());

        user.update("Maria Nova", "nova@email.com", "nova", "654321", userType("DONO_RESTAURANTE"), address());

        assertEquals("Maria Nova", user.getName());
        assertEquals("DONO_RESTAURANTE", user.getUserType().getName());
    }

    @Test
    void shouldValidateRestaurantInvariants() {
        User owner = owner();

        assertThrows(BusinessException.class, () -> Restaurant.create(" ", "Rua A", "Brasileira", "11-23", owner));
        assertThrows(BusinessException.class, () -> Restaurant.create("Nome", " ", "Brasileira", "11-23", owner));
        assertThrows(BusinessException.class, () -> Restaurant.create("Nome", "Rua A", " ", "11-23", owner));
        assertThrows(BusinessException.class, () -> Restaurant.create("Nome", "Rua A", "Brasileira", " ", owner));
        assertThrows(BusinessException.class, () -> Restaurant.create("Nome", "Rua A", "Brasileira", "11-23", null));
    }

    @Test
    void shouldUpdateValidRestaurant() {
        Restaurant restaurant = Restaurant.create("Nome", "Rua A", "Brasileira", "11-23", owner());

        restaurant.update("Novo Nome", "Rua B", "Italiana", "12-22", owner());

        assertEquals("Novo Nome", restaurant.getName());
        assertEquals("Italiana", restaurant.getCuisineType());
    }

    @Test
    void shouldValidateMenuItemInvariants() {
        Restaurant restaurant = restaurant();

        assertThrows(BusinessException.class, () -> MenuItem.create(" ", null, BigDecimal.ONE, false, null, restaurant));
        assertThrows(BusinessException.class, () -> MenuItem.create("Item", null, null, false, null, restaurant));
        assertThrows(BusinessException.class, () -> MenuItem.create("Item", null, BigDecimal.ZERO, false, null, restaurant));
        assertThrows(BusinessException.class, () -> MenuItem.create("Item", null, BigDecimal.ONE, false, null, null));
    }

    @Test
    void shouldUpdateValidMenuItem() {
        MenuItem item = MenuItem.create("Item", null, BigDecimal.ONE, false, null, restaurant());

        item.update("Novo Item", "Descricao", BigDecimal.TEN, true, "/photo.jpg", restaurant());

        assertEquals("Novo Item", item.getName());
        assertEquals(BigDecimal.TEN, item.getPrice());
        assertTrue(item.isAvailableOnlyInRestaurant());
    }

    private Address address() {
        return new Address("Rua A", "10", "Centro", "Sao Paulo", "SP", "01000-000", null);
    }

    private UserType userType(String name) {
        LocalDateTime now = LocalDateTime.now();
        return UserType.restore(1L, name, null, true, now, now);
    }

    private User owner() {
        return User.restore(
                1L,
                "Joao",
                "joao@email.com",
                "joao",
                "123456",
                LocalDateTime.now(),
                userType("DONO_RESTAURANTE"),
                address()
        );
    }

    private Restaurant restaurant() {
        return Restaurant.restore(1L, "Sabor Brasil", "Rua A", "Brasileira", "11-23", owner());
    }
}
