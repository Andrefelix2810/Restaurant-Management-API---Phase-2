package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserTypeRepository;
import com.restaurantsystem.restaurantmanagementapi.mapper.UserTypeMapper;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserTypeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceImplTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    private UserTypeServiceImpl userTypeService;

    @BeforeEach
    void setUp() {
        userTypeService = new UserTypeServiceImpl(userTypeRepository, new UserTypeMapper());
    }

    @Test
    void shouldCreateUserTypeSuccessfully() {
        UserTypeCreateRequest request = new UserTypeCreateRequest("CUSTOMER", "Restaurant customer");
        UserType savedUserType = new UserType();
        savedUserType.setId(1L);
        savedUserType.setName(request.getName());
        savedUserType.setDescription(request.getDescription());
        savedUserType.setActive(true);

        when(userTypeRepository.existsByName(request.getName())).thenReturn(false);
        when(userTypeRepository.save(any(UserType.class))).thenReturn(savedUserType);

        UserTypeResponse response = userTypeService.create(request);

        assertEquals(1L, response.getId());
        assertEquals("CUSTOMER", response.getName());
        assertTrue(response.getActive());
        verify(userTypeRepository).save(any(UserType.class));
    }

    @Test
    void shouldPreventDuplicatedUserType() {
        UserTypeCreateRequest request = new UserTypeCreateRequest("CUSTOMER", "Restaurant customer");

        when(userTypeRepository.existsByName(request.getName())).thenReturn(true);

        assertThrows(BusinessException.class, () -> userTypeService.create(request));
        verify(userTypeRepository, never()).save(any(UserType.class));
    }
}
