package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantsystem.restaurantmanagementapi.application.service.UserTypeService;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeUpdateRequest;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserTypeController.class)
class UserTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserTypeService userTypeService;

    @Test
    void shouldCreateUserType() throws Exception {
        when(userTypeService.create(any(UserTypeCreateRequest.class)))
                .thenReturn(userTypeResponse(1L, "CUSTOMER"));

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserTypeCreateRequest("CUSTOMER"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("CUSTOMER"));
    }

    @Test
    void shouldListUserTypes() throws Exception {
        when(userTypeService.findAll()).thenReturn(List.of(
                userTypeResponse(1L, "CUSTOMER"),
                userTypeResponse(2L, "RESTAURANT_OWNER")
        ));

        mockMvc.perform(get("/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("CUSTOMER"))
                .andExpect(jsonPath("$[1].name").value("RESTAURANT_OWNER"));
    }

    @Test
    void shouldFindUserTypeById() throws Exception {
        when(userTypeService.findById(1L)).thenReturn(userTypeResponse(1L, "CUSTOMER"));

        mockMvc.perform(get("/user-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("CUSTOMER"));
    }

    @Test
    void shouldReturnNotFoundWhenUserTypeDoesNotExist() throws Exception {
        when(userTypeService.findById(1L)).thenThrow(new ResourceNotFoundException("UserType", 1L));

        mockMvc.perform(get("/user-types/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("UserType not found with id: 1"));
    }

    @Test
    void shouldUpdateUserTypeWithNameOnly() throws Exception {
        when(userTypeService.update(eq(1L), any(UserTypeUpdateRequest.class)))
                .thenReturn(userTypeResponse(1L, "RESTAURANT_OWNER"));

        mockMvc.perform(put("/user-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"RESTAURANT_OWNER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("RESTAURANT_OWNER"));
    }

    @Test
    void shouldDeleteUserType() throws Exception {
        doNothing().when(userTypeService).delete(1L);

        mockMvc.perform(delete("/user-types/1"))
                .andExpect(status().isNoContent());
    }

    private UserTypeResponse userTypeResponse(Long id, String name) {
        return new UserTypeResponse(id, name);
    }
}
