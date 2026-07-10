package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.service.UserTypeService;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
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
    private final UserTypeMapper userTypeMapper;

    public UserTypeServiceImpl(UserTypeRepository userTypeRepository, UserTypeMapper userTypeMapper) {
        this.userTypeRepository = userTypeRepository;
        this.userTypeMapper = userTypeMapper;
    }

    @Override
    public UserTypeResponse create(UserTypeCreateRequest request) {
        if (userTypeRepository.existsByName(request.getName())) {
            throw new BusinessException("User type already registered");
        }

        UserType userType = userTypeMapper.toEntity(request);
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

        if (!userType.getName().equals(request.getName()) && userTypeRepository.existsByName(request.getName())) {
            throw new BusinessException("User type already registered");
        }

        userTypeMapper.updateEntity(userType, request);
        UserType updatedUserType = userTypeRepository.save(userType);
        return userTypeMapper.toResponse(updatedUserType);
    }

    @Override
    public void delete(Long id) {
        UserType userType = findUserType(id);
        userType.setActive(false);
        userTypeRepository.save(userType);
    }

    private UserType findUserType(Long id) {
        return userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserType", id));
    }
}
