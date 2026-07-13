package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantsystem.restaurantmanagementapi.application.service.MenuItemService;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemRestaurantResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({MenuItemController.class, RestaurantMenuItemController.class})
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuItemService menuItemService;

    @Test
    void shouldCreateMenuItem() throws Exception {
        when(menuItemService.create(any(MenuItemCreateRequest.class))).thenReturn(response(1L));

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Feijoada"))
                .andExpect(jsonPath("$.price").value(39.90))
                .andExpect(jsonPath("$.restaurant.id").value(1));
    }

    @Test
    void shouldListAllMenuItems() throws Exception {
        when(menuItemService.findAll()).thenReturn(List.of(response(1L), response(2L)));

        mockMvc.perform(get("/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void shouldFindMenuItemById() throws Exception {
        when(menuItemService.findById(1L)).thenReturn(response(1L));

        mockMvc.perform(get("/menu-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photoPath").value("/images/feijoada.jpg"));
    }

    @Test
    void shouldListMenuItemsByRestaurant() throws Exception {
        when(menuItemService.findByRestaurantId(1L)).thenReturn(List.of(response(1L)));

        mockMvc.perform(get("/restaurants/1/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].restaurant.id").value(1));
    }

    @Test
    void shouldUpdateMenuItem() throws Exception {
        when(menuItemService.update(eq(1L), any(MenuItemUpdateRequest.class))).thenReturn(response(1L));

        mockMvc.perform(put("/menu-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldDeleteMenuItem() throws Exception {
        mockMvc.perform(delete("/menu-items/1"))
                .andExpect(status().isNoContent());

        verify(menuItemService).delete(1L);
    }

    @Test
    void shouldRejectBlankName() throws Exception {
        MenuItemCreateRequest request = createRequest();
        request.setName(" ");

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name: Name is required"));
    }

    @Test
    void shouldRejectNonPositivePrice() throws Exception {
        MenuItemCreateRequest request = createRequest();
        request.setPrice(BigDecimal.ZERO);

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("price: Price must be greater than zero"));
    }

    @Test
    void shouldRejectMissingRestaurant() throws Exception {
        MenuItemCreateRequest request = createRequest();
        request.setRestaurantId(null);

        mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("restaurantId: Restaurant is required"));
    }

    @Test
    void shouldReturnNotFoundWhenMenuItemDoesNotExist() throws Exception {
        when(menuItemService.findById(99L)).thenThrow(new ResourceNotFoundException("Menu item", 99L));

        mockMvc.perform(get("/menu-items/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Menu item not found with id: 99"));
    }

    private MenuItemCreateRequest createRequest() {
        return new MenuItemCreateRequest(
                "Feijoada", "Feijoada completa", new BigDecimal("39.90"),
                true, "/images/feijoada.jpg", 1L
        );
    }

    private MenuItemUpdateRequest updateRequest() {
        return new MenuItemUpdateRequest(
                "Feijoada completa", "Serve duas pessoas", new BigDecimal("49.90"),
                false, "/images/feijoada-completa.jpg", 1L
        );
    }

    private MenuItemResponse response(Long id) {
        return new MenuItemResponse(
                id, "Feijoada", "Feijoada completa", new BigDecimal("39.90"),
                true, "/images/feijoada.jpg", new MenuItemRestaurantResponse(1L, "Restaurante Sabor Brasil")
        );
    }
}
