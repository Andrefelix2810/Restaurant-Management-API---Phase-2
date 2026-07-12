package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserTypeRepository;
import com.restaurantsystem.restaurantmanagementapi.mapper.UserTypeMapper;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserTypeResponse;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceImplTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private UserRepository userRepository;

    private UserTypeServiceImpl userTypeService;

    @BeforeEach
    void setUp() {
        userTypeService = new UserTypeServiceImpl(userTypeRepository, userRepository, new UserTypeMapper());
    }

    @Test
    void shouldCreateUserTypeSuccessfully() {
        UserTypeCreateRequest request = new UserTypeCreateRequest(" CUSTOMER ");

        when(userTypeRepository.existsByNameIgnoreCase("CUSTOMER")).thenReturn(false);
        when(userTypeRepository.save(any(UserType.class))).thenAnswer(invocation -> {
            UserType userType = invocation.getArgument(0);
            userType.setId(1L);
            return userType;
        });

        UserTypeResponse response = userTypeService.create(request);

        assertEquals(1L, response.getId());
        assertEquals("CUSTOMER", response.getName());
        verify(userTypeRepository).save(any(UserType.class));
    }

    @Test
    void shouldPreventDuplicatedUserType() {
        UserTypeCreateRequest request = new UserTypeCreateRequest("CUSTOMER");

        when(userTypeRepository.existsByNameIgnoreCase("CUSTOMER")).thenReturn(true);

        assertThrows(BusinessException.class, () -> userTypeService.create(request));
        verify(userTypeRepository, never()).save(any(UserType.class));
    }

    @Test
    void shouldListUserTypes() {
        when(userTypeRepository.findAll()).thenReturn(List.of(
                userType(1L, "CUSTOMER"),
                userType(2L, "RESTAURANT_OWNER")
        ));

        List<UserTypeResponse> response = userTypeService.findAll();

        assertEquals(2, response.size());
        assertEquals("CUSTOMER", response.get(0).getName());
        assertEquals("RESTAURANT_OWNER", response.get(1).getName());
    }

    @Test
    void shouldFindUserTypeByIdSuccessfully() {
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType(1L, "CUSTOMER")));

        UserTypeResponse response = userTypeService.findById(1L);

        assertEquals(1L, response.getId());
        assertEquals("CUSTOMER", response.getName());
    }

    @Test
    void shouldThrowExceptionWhenFindByIdDoesNotExist() {
        when(userTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userTypeService.findById(1L));
    }

    @Test
    void shouldUpdateUserTypeSuccessfully() {
        UserTypeUpdateRequest request = new UserTypeUpdateRequest(" RESTAURANT_OWNER ");

        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType(1L, "CUSTOMER")));
        when(userTypeRepository.existsByNameIgnoreCase("RESTAURANT_OWNER")).thenReturn(false);
        when(userTypeRepository.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserTypeResponse response = userTypeService.update(1L, request);

        assertEquals(1L, response.getId());
        assertEquals("RESTAURANT_OWNER", response.getName());
        verify(userTypeRepository).save(any(UserType.class));
    }

    @Test
    void shouldNotCheckDuplicityWhenUpdatingKeepingSameNameIgnoringCase() {
        UserTypeUpdateRequest request = new UserTypeUpdateRequest(" customer ");

        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType(1L, "CUSTOMER")));
        when(userTypeRepository.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserTypeResponse response = userTypeService.update(1L, request);

        assertEquals("customer", response.getName());
        verify(userTypeRepository, never()).existsByNameIgnoreCase(anyString());
        verify(userTypeRepository).save(any(UserType.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentUserType() {
        UserTypeUpdateRequest request = new UserTypeUpdateRequest("CUSTOMER");

        when(userTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userTypeService.update(1L, request));
        verify(userTypeRepository, never()).save(any(UserType.class));
    }

    @Test
    void shouldPreventUpdatingToDuplicatedName() {
        UserTypeUpdateRequest request = new UserTypeUpdateRequest("RESTAURANT_OWNER");

        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType(1L, "CUSTOMER")));
        when(userTypeRepository.existsByNameIgnoreCase("RESTAURANT_OWNER")).thenReturn(true);

        assertThrows(BusinessException.class, () -> userTypeService.update(1L, request));
        verify(userTypeRepository, never()).save(any(UserType.class));
    }

    @Test
    void shouldDeleteUserTypeSuccessfully() {
        UserType userType = userType(1L, "CUSTOMER");

        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType));
        when(userRepository.existsByUserTypeId(1L)).thenReturn(false);

        userTypeService.delete(1L);

        verify(userTypeRepository).delete(userType);
    }

    @Test
    void shouldPreventDeletingUserTypeAssociatedWithUsers() {
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType(1L, "CUSTOMER")));
        when(userRepository.existsByUserTypeId(1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> userTypeService.delete(1L));
        verify(userTypeRepository, never()).delete(any(UserType.class));
    }

    private UserType userType(Long id, String name) {
        UserType userType = new UserType();
        userType.setId(id);
        userType.setName(name);
        userType.setDescription(name + " description");
        userType.setActive(true);
        return userType;
    }
}
