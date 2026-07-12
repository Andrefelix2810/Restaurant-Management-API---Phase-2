package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.service.UserTypeService;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserTypeRepository;
import com.restaurantsystem.restaurantmanagementapi.mapper.UserTypeMapper;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserTypeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;
    private final UserTypeMapper userTypeMapper;

    public UserTypeServiceImpl(
            UserTypeRepository userTypeRepository,
            UserRepository userRepository,
            UserTypeMapper userTypeMapper
    ) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
        this.userTypeMapper = userTypeMapper;
    }

    @Override
    public UserTypeResponse create(UserTypeCreateRequest request) {
        String name = normalizeName(request.getName());

        if (userTypeRepository.existsByNameIgnoreCase(name)) {
            throw new BusinessException("User type already registered");
        }

        UserType userType = userTypeMapper.toEntity(request);
        userType.setName(name);
        UserType savedUserType = userTypeRepository.save(userType);
        return userTypeMapper.toResponse(savedUserType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserTypeResponse> findAll() {
        return userTypeRepository.findAll()
                .stream()
                .map(userTypeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserTypeResponse findById(Long id) {
        UserType userType = findUserType(id);
        return userTypeMapper.toResponse(userType);
    }

    @Override
    public UserTypeResponse update(Long id, UserTypeUpdateRequest request) {
        UserType userType = findUserType(id);
        String name = normalizeName(request.getName());

        if (!userType.getName().equalsIgnoreCase(name) && userTypeRepository.existsByNameIgnoreCase(name)) {
            throw new BusinessException("User type already registered");
        }

        userTypeMapper.updateEntity(userType, request);
        userType.setName(name);
        UserType updatedUserType = userTypeRepository.save(userType);
        return userTypeMapper.toResponse(updatedUserType);
    }

    @Override
    public void delete(Long id) {
        UserType userType = findUserType(id);

        if (userRepository.existsByUserTypeId(id)) {
            throw new BusinessException("User type is associated with users and cannot be deleted");
        }

        userTypeRepository.delete(userType);
    }

    private UserType findUserType(Long id) {
        return userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserType", id));
    }

    private String normalizeName(String name) {
        return name == null ? null : name.trim();
    }
}
