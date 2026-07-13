package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantsystem.restaurantmanagementapi.application.service.RestaurantService;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.RestaurantOwnerResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.RestaurantResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserTypeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    void shouldCreateRestaurant() throws Exception {
        when(restaurantService.create(any(RestaurantCreateRequest.class))).thenReturn(restaurantResponse(1L));

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Restaurante Sabor Brasil"))
                .andExpect(jsonPath("$.owner.id").value(1))
                .andExpect(jsonPath("$.owner.email").value("joao@email.com"))
                .andExpect(jsonPath("$.owner.userType.name").value("DONO_RESTAURANTE"));
    }

    @Test
    void shouldListRestaurants() throws Exception {
        when(restaurantService.findAll()).thenReturn(List.of(restaurantResponse(1L), restaurantResponse(2L)));

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void shouldFindRestaurantById() throws Exception {
        when(restaurantService.findById(1L)).thenReturn(restaurantResponse(1L));

        mockMvc.perform(get("/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cuisineType").value("Brasileira"));
    }

    @Test
    void shouldReturnNotFoundWhenRestaurantDoesNotExist() throws Exception {
        when(restaurantService.findById(1L)).thenThrow(new ResourceNotFoundException("Restaurant", 1L));

        mockMvc.perform(get("/restaurants/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Restaurant not found with id: 1"));
    }

    @Test
    void shouldUpdateRestaurant() throws Exception {
        when(restaurantService.update(eq(1L), any(RestaurantUpdateRequest.class))).thenReturn(restaurantResponse(1L));

        mockMvc.perform(put("/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.openingHours").value("Segunda a sabado, das 11h as 23h"));
    }

    @Test
    void shouldDeleteRestaurant() throws Exception {
        doNothing().when(restaurantService).delete(1L);

        mockMvc.perform(delete("/restaurants/1"))
                .andExpect(status().isNoContent());

        verify(restaurantService).delete(1L);
    }

    @Test
    void shouldReturnBadRequestWhenRequiredFieldIsMissing() throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"Rua das Flores, 123\",\"cuisineType\":\"Brasileira\",\"openingHours\":\"11h as 23h\",\"ownerId\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenJsonIsMalformed() throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Restaurante Sabor Brasil\",\"ownerId\""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid or malformed JSON request body"));
    }

    private RestaurantCreateRequest createRequest() {
        return new RestaurantCreateRequest(
                "Restaurante Sabor Brasil",
                "Rua das Flores, 123 - Sao Paulo/SP",
                "Brasileira",
                "Segunda a sabado, das 11h as 23h",
                1L
        );
    }

    private RestaurantUpdateRequest updateRequest() {
        return new RestaurantUpdateRequest(
                "Restaurante Sabor Brasil",
                "Rua das Flores, 123 - Sao Paulo/SP",
                "Brasileira",
                "Segunda a sabado, das 11h as 23h",
                1L
        );
    }

    private RestaurantResponse restaurantResponse(Long id) {
        RestaurantOwnerResponse owner = new RestaurantOwnerResponse(
                1L,
                "Joao Silva",
                "joao@email.com",
                new UserTypeResponse(2L, "DONO_RESTAURANTE")
        );

        return new RestaurantResponse(
                id,
                "Restaurante Sabor Brasil",
                "Rua das Flores, 123 - Sao Paulo/SP",
                "Brasileira",
                "Segunda a sabado, das 11h as 23h",
                owner
        );
    }
}
