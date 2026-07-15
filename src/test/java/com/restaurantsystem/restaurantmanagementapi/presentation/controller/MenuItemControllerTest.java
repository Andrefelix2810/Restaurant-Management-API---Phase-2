package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.MenuItemUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.MenuItemCommand;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuItemController.class)
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuItemUseCase menuItemUseCase;

    @Test
    void shouldCreateMenuItem() throws Exception {
        when(menuItemUseCase.create(command())).thenReturn(menuItem());

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.restaurant.name").value("Sabor Brasil"));
    }

    @Test
    void shouldRejectNonPositivePrice() throws Exception {
        MenuItemCreateRequest request = request();
        request.setPrice(BigDecimal.ZERO);

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("price: Price must be greater than zero"));
    }

    @Test
    void shouldRejectMissingRestaurant() throws Exception {
        MenuItemCreateRequest request = request();
        request.setRestaurantId(null);

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldListMenuItems() throws Exception {
        when(menuItemUseCase.findAll()).thenReturn(List.of(menuItem()));

        mockMvc.perform(get("/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Feijoada"));
    }

    @Test
    void shouldDeleteMenuItem() throws Exception {
        mockMvc.perform(delete("/menu-items/1"))
                .andExpect(status().isNoContent());

        verify(menuItemUseCase).delete(1L);
    }

    private MenuItemCreateRequest request() {
        return new MenuItemCreateRequest(
                "Feijoada",
                "Feijoada completa",
                new BigDecimal("39.90"),
                true,
                "/images/feijoada.jpg",
                1L
        );
    }

    private MenuItemCommand command() {
        MenuItemCreateRequest request = request();
        return new MenuItemCommand(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.isAvailableOnlyInRestaurant(),
                request.getPhotoPath(),
                request.getRestaurantId()
        );
    }

    private MenuItem menuItem() {
        LocalDateTime now = LocalDateTime.now();
        UserType type = UserType.restore(2L, "DONO_RESTAURANTE", null, true, now, now);
        Address address = new Address("Rua A", "10", "Centro", "Sao Paulo", "SP", "01000-000", null);
        User owner = User.restore(1L, "Joao", "joao@email.com", "joao", "123456", now, type, address);
        Restaurant restaurant = Restaurant.restore(1L, "Sabor Brasil", "Rua A", "Brasileira", "11:00-23:00", owner);
        return MenuItem.restore(
                1L,
                "Feijoada",
                "Feijoada completa",
                new BigDecimal("39.90"),
                true,
                "/images/feijoada.jpg",
                restaurant
        );
    }
}
