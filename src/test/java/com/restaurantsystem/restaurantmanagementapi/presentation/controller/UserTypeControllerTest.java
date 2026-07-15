package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.UserTypeUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.UserTypeCommand;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeCreateRequest;
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

@WebMvcTest(UserTypeController.class)
class UserTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserTypeUseCase userTypeUseCase;

    @Test
    void shouldCreateUserType() throws Exception {
        when(userTypeUseCase.create(new UserTypeCommand("CLIENTE"))).thenReturn(userType(1L, "CLIENTE"));

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserTypeCreateRequest("CLIENTE"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("CLIENTE"));
    }

    @Test
    void shouldRejectBlankName() throws Exception {
        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\" \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectUserTypeOutsideClosedCatalog() throws Exception {
        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ADMIN\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "name: Invalid user type. Allowed values: CLIENTE or DONO_RESTAURANTE"
                ));
    }

    @Test
    void shouldListUserTypes() throws Exception {
        when(userTypeUseCase.findAll()).thenReturn(List.of(
                userType(1L, "CLIENTE"),
                userType(2L, "DONO_RESTAURANTE")
        ));

        mockMvc.perform(get("/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("CLIENTE"))
                .andExpect(jsonPath("$[1].name").value("DONO_RESTAURANTE"));
    }

    @Test
    void shouldDeleteUserType() throws Exception {
        mockMvc.perform(delete("/user-types/1"))
                .andExpect(status().isNoContent());

        verify(userTypeUseCase).delete(1L);
    }

    private UserType userType(Long id, String name) {
        LocalDateTime now = LocalDateTime.now();
        return UserType.restore(id, name, null, true, now, now);
    }
}
