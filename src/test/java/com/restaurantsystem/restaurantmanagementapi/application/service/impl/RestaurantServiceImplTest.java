package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.RestaurantRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserRepository;
import com.restaurantsystem.restaurantmanagementapi.mapper.RestaurantMapper;
import com.restaurantsystem.restaurantmanagementapi.mapper.UserTypeMapper;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.RestaurantResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    private RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
        RestaurantMapper restaurantMapper = new RestaurantMapper(new UserTypeMapper());
        restaurantService = new RestaurantServiceImpl(restaurantRepository, userRepository, restaurantMapper);
    }

    @Test
    void shouldCreateRestaurantSuccessfully() {
        RestaurantCreateRequest request = createRequest();
        User owner = owner(1L, "DONO_RESTAURANTE");

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> {
            Restaurant restaurant = invocation.getArgument(0);
            restaurant.setId(1L);
            return restaurant;
        });

        RestaurantResponse response = restaurantService.create(request);

        assertEquals(1L, response.getId());
        assertEquals("Restaurante Sabor Brasil", response.getName());
        assertEquals("Brasileira", response.getCuisineType());
        assertEquals(1L, response.getOwner().getId());
        assertEquals("DONO_RESTAURANTE", response.getOwner().getUserType().getName());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdDoesNotExistOnCreate() {
        RestaurantCreateRequest request = createRequest();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.create(request));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenOwnerIsNotRestaurantOwnerOnCreate() {
        RestaurantCreateRequest request = createRequest();

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner(1L, "CLIENTE")));

        assertThrows(BusinessException.class, () -> restaurantService.create(request));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldListRestaurants() {
        when(restaurantRepository.findAll()).thenReturn(List.of(
                restaurant(1L, owner(1L, "DONO_RESTAURANTE")),
                restaurant(2L, owner(2L, "DONO_RESTAURANTE"))
        ));

        List<RestaurantResponse> response = restaurantService.findAll();

        assertEquals(2, response.size());
        assertEquals("Restaurante Sabor Brasil", response.get(0).getName());
        assertEquals("Restaurante Sabor Brasil", response.get(1).getName());
    }

    @Test
    void shouldFindRestaurantByIdSuccessfully() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant(1L, owner(1L, "DONO_RESTAURANTE"))));

        RestaurantResponse response = restaurantService.findById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Restaurante Sabor Brasil", response.getName());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantDoesNotExistOnFindById() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.findById(1L));
    }

    @Test
    void shouldUpdateRestaurantSuccessfully() {
        Restaurant restaurant = restaurant(1L, owner(1L, "DONO_RESTAURANTE"));
        User newOwner = owner(2L, "DONO_RESTAURANTE");
        RestaurantUpdateRequest request = updateRequest();

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(userRepository.findById(2L)).thenReturn(Optional.of(newOwner));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RestaurantResponse response = restaurantService.update(1L, request);

        assertEquals(1L, response.getId());
        assertEquals("Restaurante Sabor Brasil Atualizado", response.getName());
        assertEquals("Italiana", response.getCuisineType());
        assertEquals(2L, response.getOwner().getId());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantDoesNotExistOnUpdate() {
        RestaurantUpdateRequest request = updateRequest();

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.update(1L, request));
        verify(userRepository, never()).findById(any());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdDoesNotExistOnUpdate() {
        RestaurantUpdateRequest request = updateRequest();

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant(1L, owner(1L, "DONO_RESTAURANTE"))));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.update(1L, request));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldDeleteRestaurantSuccessfully() {
        Restaurant restaurant = restaurant(1L, owner(1L, "DONO_RESTAURANTE"));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        restaurantService.delete(1L);

        verify(restaurantRepository).delete(restaurant);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantDoesNotExistOnDelete() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.delete(1L));
        verify(restaurantRepository, never()).delete(any(Restaurant.class));
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
                "Restaurante Sabor Brasil Atualizado",
                "Rua das Flores, 456 - Sao Paulo/SP",
                "Italiana",
                "Todos os dias, das 12h as 22h",
                2L
        );
    }

    private Restaurant restaurant(Long id, User owner) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName("Restaurante Sabor Brasil");
        restaurant.setAddress("Rua das Flores, 123 - Sao Paulo/SP");
        restaurant.setCuisineType("Brasileira");
        restaurant.setOpeningHours("Segunda a sabado, das 11h as 23h");
        restaurant.setOwner(owner);
        return restaurant;
    }

    private User owner(Long id, String userTypeName) {
        UserType userType = new UserType();
        userType.setId(2L);
        userType.setName(userTypeName);

        User owner = new User();
        owner.setId(id);
        owner.setName("Joao Silva");
        owner.setEmail("joao" + id + "@email.com");
        owner.setUserType(userType);
        return owner;
    }
}
