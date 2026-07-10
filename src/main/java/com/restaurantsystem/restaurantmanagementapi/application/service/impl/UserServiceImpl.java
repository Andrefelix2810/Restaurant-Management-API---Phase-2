package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.service.UserService;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserTypeRepository;
import com.restaurantsystem.restaurantmanagementapi.mapper.UserMapper;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(
            UserRepository userRepository,
            UserTypeRepository userTypeRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse create(UserCreateRequest request) {
        validateEmailAndLoginForCreate(request);

        UserType userType = findUserType(request.getUserTypeId());
        User user = userMapper.toEntity(request);
        user.setUserType(userType);

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        validateEmailAndLoginForUpdate(id, request);

        UserType userType = findUserType(request.getUserTypeId());
        userMapper.updateEntity(user, request, userType);

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

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

    private UserType findUserType(Long userTypeId) {
        return userTypeRepository.findById(userTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("UserType", userTypeId));
    }
}
