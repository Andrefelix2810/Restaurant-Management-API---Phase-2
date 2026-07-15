package com.restaurantsystem.restaurantmanagementapi.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MainFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldExecuteMainRestaurantManagementFlow() throws Exception {
        Long userTypeId = createAndReturnId("/user-types", """
                {"name":"DONO_RESTAURANTE"}
                """);

        Long ownerId = createAndReturnId("/users", """
                {
                  "name":"Joao Silva",
                  "email":"joao.integration@email.com",
                  "login":"joao.integration",
                  "password":"123456",
                  "userTypeId":%d,
                  "address":{
                    "street":"Rua Central",
                    "number":"100",
                    "neighborhood":"Centro",
                    "city":"Sao Paulo",
                    "state":"SP",
                    "zipCode":"01001000",
                    "complement":"Sala 12"
                  }
                }
                """.formatted(userTypeId));

        Long restaurantId = createAndReturnId("/restaurants", """
                {
                  "name":"Restaurante Integracao",
                  "address":"Rua das Flores, 123",
                  "cuisineType":"Brasileira",
                  "openingHours":"11h as 23h",
                  "ownerId":%d
                }
                """.formatted(ownerId));

        Long menuItemId = createAndReturnId("/menu-items", """
                {
                  "name":"Feijoada",
                  "description":"Feijoada completa",
                  "price":39.90,
                  "availableOnlyInRestaurant":true,
                  "photoPath":"/images/feijoada.jpg",
                  "restaurantId":%d
                }
                """.formatted(restaurantId));

        mockMvc.perform(get("/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userTypeId));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ownerId))
                .andExpect(jsonPath("$[0].userType.name").value("DONO_RESTAURANTE"));

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(restaurantId));

        mockMvc.perform(get("/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(menuItemId));

        mockMvc.perform(get("/restaurants/{restaurantId}/menu-items", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].restaurant.id").value(restaurantId));
    }

    @Test
    void shouldExposeDocumentedBadRequestResponseInOpenApi() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/user-types'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/restaurants'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/menu-items'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/user-types/{id}'].delete.responses['204']").exists())
                .andExpect(jsonPath("$.paths['/users/{id}'].delete.responses['204']").exists())
                .andExpect(jsonPath("$.paths['/restaurants/{id}'].delete.responses['204']").exists())
                .andExpect(jsonPath("$.paths['/menu-items/{id}'].delete.responses['204']").exists())
                .andExpect(jsonPath("$.components.schemas.UserTypeCreateRequest.properties.name.enum[0]")
                        .value("CLIENTE"))
                .andExpect(jsonPath("$.components.schemas.UserTypeCreateRequest.properties.name.enum[1]")
                        .value("DONO_RESTAURANTE"));
    }

    @Test
    void shouldReuseTheTwoAllowedUserTypesAcrossMultipleUsers() throws Exception {
        Long customerTypeId = createAndReturnId("/user-types", "{\"name\":\"CLIENTE\"}");
        Long ownerTypeId = createAndReturnId("/user-types", "{\"name\":\"DONO_RESTAURANTE\"}");

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ADMIN\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"CLIENTE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "User type already registered. Reuse its id when creating users"
                ));

        for (int index = 1; index <= 5; index++) {
            Long userTypeId = index <= 3 ? customerTypeId : ownerTypeId;
            createAndReturnId("/users", userRequest(index, userTypeId));
        }

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].userType.id").value(customerTypeId));
    }

    private String userRequest(int index, Long userTypeId) {
        return """
                {
                  "name":"Usuario %d",
                  "email":"usuario%d@email.com",
                  "login":"usuario%d",
                  "password":"123456",
                  "userTypeId":%d,
                  "address":{
                    "street":"Rua Central",
                    "number":"%d",
                    "neighborhood":"Centro",
                    "city":"Sao Paulo",
                    "state":"SP",
                    "zipCode":"01001000"
                  }
                }
                """.formatted(index, index, index, userTypeId, index);
    }

    private Long createAndReturnId(String endpoint, String body) throws Exception {
        String response = mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        return json.get("id").asLong();
    }
}
