package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.AddressCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.UserCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserTypePersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserPersistencePort userGateway;

    @Mock
    private UserTypePersistencePort userTypeGateway;

    private UserServiceImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new UserServiceImpl(userGateway, userTypeGateway);
    }

    @Test
    void shouldCreateUser() {
        UserType userType = userType();
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType));
        when(userGateway.save(any(User.class))).thenAnswer(invocation -> withId(invocation.getArgument(0), 10L));

        User created = useCase.create(command());

        assertEquals(10L, created.getId());
        assertEquals("Maria Silva", created.getName());
        assertEquals(userType, created.getUserType());
        verify(userGateway).save(any(User.class));
    }

    @Test
    void shouldRejectDuplicatedEmailOnCreate() {
        when(userGateway.existsByEmail("maria@email.com")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> useCase.create(command()));

        assertEquals("Email already registered", exception.getMessage());
        verify(userGateway, never()).save(any());
    }

    @Test
    void shouldRejectDuplicatedLoginOnCreate() {
        when(userGateway.existsByLogin("maria")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> useCase.create(command()));

        assertEquals("Login already registered", exception.getMessage());
    }

    @Test
    void shouldRejectUnknownUserTypeOnCreate() {
        when(userTypeGateway.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.create(command()));
    }

    @Test
    void shouldFindAllUsers() {
        when(userGateway.findAll()).thenReturn(List.of(user(1L), user(2L)));

        List<User> result = useCase.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldFindUserById() {
        when(userGateway.findById(1L)).thenReturn(Optional.of(user(1L)));

        assertEquals(1L, useCase.findById(1L).getId());
    }

    @Test
    void shouldRejectUnknownUserId() {
        when(userGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findById(99L));
    }

    @Test
    void shouldUpdateUser() {
        User existing = user(1L);
        when(userGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType()));
        when(userGateway.save(existing)).thenReturn(existing);

        User updated = useCase.update(1L, command());

        assertEquals("Maria Silva", updated.getName());
        verify(userGateway).save(existing);
    }

    @Test
    void shouldRejectEmailUsedByAnotherUserOnUpdate() {
        when(userGateway.findById(1L)).thenReturn(Optional.of(user(1L)));
        when(userGateway.findByEmail("maria@email.com")).thenReturn(Optional.of(user(2L)));

        assertThrows(BusinessException.class, () -> useCase.update(1L, command()));
    }

    @Test
    void shouldRejectLoginUsedByAnotherUserOnUpdate() {
        when(userGateway.findById(1L)).thenReturn(Optional.of(user(1L)));
        when(userGateway.findByLogin("maria")).thenReturn(Optional.of(user(2L)));

        assertThrows(BusinessException.class, () -> useCase.update(1L, command()));
    }

    @Test
    void shouldAllowSameEmailAndLoginOnUpdate() {
        User existing = user(1L);
        when(userGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(userGateway.findByEmail("maria@email.com")).thenReturn(Optional.of(existing));
        when(userGateway.findByLogin("maria")).thenReturn(Optional.of(existing));
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType()));
        when(userGateway.save(existing)).thenReturn(existing);

        User updated = useCase.update(1L, command());

        assertEquals(1L, updated.getId());
    }

    @Test
    void shouldDeleteUser() {
        User user = user(1L);
        when(userGateway.findById(1L)).thenReturn(Optional.of(user));

        useCase.delete(1L);

        verify(userGateway).delete(user);
    }

    @Test
    void shouldPreserveLastModifiedDateWhenRestoringUser() {
        User restored = user(1L);

        assertTrue(restored.getLastModifiedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    private UserCommand command() {
        return new UserCommand(
                "Maria Silva",
                "maria@email.com",
                "maria",
                "123456",
                1L,
                new AddressCommand("Rua A", "10", "Centro", "Sao Paulo", "SP", "01000-000", null)
        );
    }

    private UserType userType() {
        LocalDateTime now = LocalDateTime.now();
        return UserType.restore(1L, "CLIENTE", null, true, now, now);
    }

    private User user(Long id) {
        return User.restore(
                id,
                "Maria Silva",
                "maria@email.com",
                "maria",
                "123456",
                LocalDateTime.now(),
                userType(),
                address()
        );
    }

    private User withId(User user, Long id) {
        return User.restore(
                id,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword(),
                user.getLastModifiedDate(),
                user.getUserType(),
                user.getAddress()
        );
    }

    private Address address() {
        return new Address("Rua A", "10", "Centro", "Sao Paulo", "SP", "01000-000", null);
    }
}
