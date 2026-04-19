package com.restaurantsystem.restaurantmanagementapi.service.impl;

import com.restaurantsystem.restaurantmanagementapi.dto.request.UserCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.UserUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.UserResponse;
import com.restaurantsystem.restaurantmanagementapi.entity.User;
import com.restaurantsystem.restaurantmanagementapi.enums.Role;
import com.restaurantsystem.restaurantmanagementapi.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.exception.UserNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.mapper.UserMapper;
import com.restaurantsystem.restaurantmanagementapi.repository.UserRepository;
import com.restaurantsystem.restaurantmanagementapi.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse create(UserCreateRequest request, Role role) {
        validateEmailAndLoginForCreate(request);

        User user = userMapper.toEntity(request);
        user.setRole(role);
        user.setLastModifiedDate(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        validateEmailAndLoginForUpdate(id, request);

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setRole(request.getRole());
        user.setAddress(userMapper.toAddressEntity(request.getAddress()));
        user.setLastModifiedDate(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }

    private void validateEmailAndLoginForCreate(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered");
        }

        if (userRepository.existsByLogin(request.getLogin())) {
            throw new BusinessException("Login already registered");
        }
    }

    private void validateEmailAndLoginForUpdate(Long id, UserUpdateRequest request) {
        userRepository.findByEmail(request.getEmail())
                .filter(foundUser -> !foundUser.getId().equals(id))
                .ifPresent(foundUser -> {
                    throw new BusinessException("Email already registered");
                });

        userRepository.findByLogin(request.getLogin())
                .filter(foundUser -> !foundUser.getId().equals(id))
                .ifPresent(foundUser -> {
                    throw new BusinessException("Login already registered");
                });
    }
}