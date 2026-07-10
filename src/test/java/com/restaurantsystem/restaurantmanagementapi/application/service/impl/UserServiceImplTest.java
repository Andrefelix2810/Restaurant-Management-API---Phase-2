package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserTypeRepository;
import com.restaurantsystem.restaurantmanagementapi.mapper.AddressMapper;
import com.restaurantsystem.restaurantmanagementapi.mapper.UserMapper;
import com.restaurantsystem.restaurantmanagementapi.mapper.UserTypeMapper;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.AddressRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTypeRepository userTypeRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        UserMapper userMapper = new UserMapper(new AddressMapper(), new UserTypeMapper());
        userService = new UserServiceImpl(userRepository, userTypeRepository, userMapper);
    }

    @Test
    void shouldCreateUserWithExistingUserType() {
        UserCreateRequest request = userRequest();
        UserType userType = userType();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByLogin(request.getLogin())).thenReturn(false);
        when(userTypeRepository.findById(request.getUserTypeId())).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            user.setUserType(userType);
            user.setAddress(address());
            return user;
        });

        UserResponse response = userService.create(request);

        assertEquals(1L, response.getId());
        assertEquals("Maria Silva", response.getName());
        assertEquals("CUSTOMER", response.getUserType().getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldPreventDuplicatedEmail() {
        UserCreateRequest request = userRequest();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.create(request));
        verify(userTypeRepository, never()).findById(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldPreventDuplicatedLogin() {
        UserCreateRequest request = userRequest();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByLogin(request.getLogin())).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.create(request));
        verify(userTypeRepository, never()).findById(any());
        verify(userRepository, never()).save(any(User.class));
    }

    private UserCreateRequest userRequest() {
        return new UserCreateRequest(
                "Maria Silva",
                "maria@email.com",
                "mariasilva",
                "123456",
                1L,
                new AddressRequest(
                        "Rua Central",
                        "100",
                        "Centro",
                        "Sao Paulo",
                        "SP",
                        "01001000",
                        "Apto 12"
                )
        );
    }

    private UserType userType() {
        UserType userType = new UserType();
        userType.setId(1L);
        userType.setName("CUSTOMER");
        userType.setDescription("Restaurant customer");
        userType.setActive(true);
        return userType;
    }

    private Address address() {
        return new Address(
                "Rua Central",
                "100",
                "Centro",
                "Sao Paulo",
                "SP",
                "01001000",
                "Apto 12"
        );
    }
}
