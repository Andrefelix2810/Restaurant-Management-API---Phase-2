package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.RestaurantUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.RestaurantCommand;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantUseCase restaurantUseCase;

    @Test
    void shouldCreateRestaurant() throws Exception {
        RestaurantCommand command = command();
        when(restaurantUseCase.create(command)).thenReturn(restaurant());

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.owner.userType.name").value("DONO_RESTAURANTE"));
    }

    @Test
    void shouldRejectRestaurantWithoutName() throws Exception {
        RestaurantCreateRequest request = request();
        request.setName(" ");

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldListRestaurants() throws Exception {
        when(restaurantUseCase.findAll()).thenReturn(List.of(restaurant()));

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sabor Brasil"));
    }

    @Test
    void shouldDeleteRestaurant() throws Exception {
        mockMvc.perform(delete("/restaurants/1"))
                .andExpect(status().isNoContent());

        verify(restaurantUseCase).delete(1L);
    }

    private RestaurantCreateRequest request() {
        return new RestaurantCreateRequest(
                "Sabor Brasil",
                "Rua das Flores, 123",
                "Brasileira",
                "11:00-23:00",
                1L
        );
    }

    private RestaurantCommand command() {
        RestaurantCreateRequest request = request();
        return new RestaurantCommand(
                request.getName(),
                request.getAddress(),
                request.getCuisineType(),
                request.getOpeningHours(),
                request.getOwnerId()
        );
    }

    private Restaurant restaurant() {
        LocalDateTime now = LocalDateTime.now();
        UserType type = UserType.restore(2L, "DONO_RESTAURANTE", null, true, now, now);
        Address address = new Address("Rua A", "10", "Centro", "Sao Paulo", "SP", "01000-000", null);
        User owner = User.restore(1L, "Joao", "joao@email.com", "joao", "123456", now, type, address);
        return Restaurant.restore(
                1L,
                "Sabor Brasil",
                "Rua das Flores, 123",
                "Brasileira",
                "11:00-23:00",
                owner
        );
    }
}
