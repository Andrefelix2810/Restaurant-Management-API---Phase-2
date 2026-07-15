package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.UserTypeCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserTypePersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceImplTest {

    @Mock
    private UserTypePersistencePort userTypeGateway;

    @Mock
    private UserPersistencePort userGateway;

    private UserTypeServiceImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new UserTypeServiceImpl(userTypeGateway, userGateway);
    }

    @Test
    void shouldCreateNormalizedUserType() {
        when(userTypeGateway.save(any(UserType.class))).thenAnswer(invocation -> withId(invocation.getArgument(0), 1L));

        UserType created = useCase.create(new UserTypeCommand("  CLIENTE  "));

        assertEquals(1L, created.getId());
        assertEquals("CLIENTE", created.getName());
    }

    @Test
    void shouldRejectDuplicatedUserType() {
        when(userTypeGateway.existsByNameIgnoreCase("CLIENTE")).thenReturn(true);

        assertThrows(BusinessException.class, () -> useCase.create(new UserTypeCommand("CLIENTE")));

        verify(userTypeGateway, never()).save(any());
    }

    @Test
    void shouldFindAllUserTypes() {
        when(userTypeGateway.findAll()).thenReturn(List.of(userType(1L, "CLIENTE"), userType(2L, "DONO_RESTAURANTE")));

        assertEquals(2, useCase.findAll().size());
    }

    @Test
    void shouldFindUserTypeById() {
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType(1L, "CLIENTE")));

        assertEquals("CLIENTE", useCase.findById(1L).getName());
    }

    @Test
    void shouldRejectUnknownUserType() {
        when(userTypeGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findById(99L));
    }

    @Test
    void shouldUpdateUserType() {
        UserType existing = userType(1L, "CLIENTE");
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(userTypeGateway.save(existing)).thenReturn(existing);

        UserType updated = useCase.update(1L, new UserTypeCommand("DONO_RESTAURANTE"));

        assertEquals("DONO_RESTAURANTE", updated.getName());
    }

    @Test
    void shouldRejectUserTypeOutsideClosedCatalog() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.create(new UserTypeCommand("ADMIN"))
        );

        assertEquals(
                "Invalid user type. Allowed values: CLIENTE or DONO_RESTAURANTE",
                exception.getMessage()
        );
        verify(userTypeGateway, never()).save(any());
    }

    @Test
    void shouldAllowUpdateWithoutChangingName() {
        UserType existing = userType(1L, "CLIENTE");
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(userTypeGateway.save(existing)).thenReturn(existing);

        assertEquals("CLIENTE", useCase.update(1L, new UserTypeCommand("cliente")).getName());
    }

    @Test
    void shouldRejectDuplicatedNameOnUpdate() {
        UserType existing = userType(1L, "CLIENTE");
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(userTypeGateway.existsByNameIgnoreCase("DONO_RESTAURANTE")).thenReturn(true);

        assertThrows(
                BusinessException.class,
                () -> useCase.update(1L, new UserTypeCommand("DONO_RESTAURANTE"))
        );
    }

    @Test
    void shouldRejectRenamingUserTypeAssociatedWithUsers() {
        UserType existing = userType(1L, "CLIENTE");
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(userGateway.existsByUserTypeId(1L)).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.update(1L, new UserTypeCommand("DONO_RESTAURANTE"))
        );

        assertEquals(
                "User type is associated with users and cannot be changed. Update the userTypeId of the user instead",
                exception.getMessage()
        );
        verify(userTypeGateway, never()).save(any());
    }

    @Test
    void shouldDeleteUnusedUserType() {
        UserType existing = userType(1L, "CLIENTE");
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(existing));

        useCase.delete(1L);

        verify(userTypeGateway).delete(existing);
    }

    @Test
    void shouldRejectDeletingUserTypeAssociatedWithUsers() {
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType(1L, "CLIENTE")));
        when(userGateway.existsByUserTypeId(1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> useCase.delete(1L));
    }

    private UserType userType(Long id, String name) {
        LocalDateTime now = LocalDateTime.now();
        return UserType.restore(id, name, null, true, now, now);
    }

    private UserType withId(UserType userType, Long id) {
        return UserType.restore(
                id,
                userType.getName(),
                userType.getDescription(),
                userType.getActive(),
                userType.getCreatedAt(),
                userType.getUpdatedAt()
        );
    }
}
