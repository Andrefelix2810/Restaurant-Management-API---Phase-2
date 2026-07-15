package com.restaurantsystem.restaurantmanagementapi.infrastructure.config;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.MenuItemUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.RestaurantUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.UserTypeUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.UserUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.MenuItemPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.RestaurantPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserTypePersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.service.impl.MenuItemServiceImpl;
import com.restaurantsystem.restaurantmanagementapi.application.service.impl.RestaurantServiceImpl;
import com.restaurantsystem.restaurantmanagementapi.application.service.impl.UserServiceImpl;
import com.restaurantsystem.restaurantmanagementapi.application.service.impl.UserTypeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    UserUseCase userUseCase(UserPersistencePort userGateway, UserTypePersistencePort userTypeGateway) {
        return new UserServiceImpl(userGateway, userTypeGateway);
    }

    @Bean
    UserTypeUseCase userTypeUseCase(UserTypePersistencePort userTypeGateway, UserPersistencePort userGateway) {
        return new UserTypeServiceImpl(userTypeGateway, userGateway);
    }

    @Bean
    RestaurantUseCase restaurantUseCase(
            RestaurantPersistencePort restaurantGateway,
            UserPersistencePort userGateway
    ) {
        return new RestaurantServiceImpl(restaurantGateway, userGateway);
    }

    @Bean
    MenuItemUseCase menuItemUseCase(
            MenuItemPersistencePort menuItemGateway,
            RestaurantPersistencePort restaurantGateway
    ) {
        return new MenuItemServiceImpl(menuItemGateway, restaurantGateway);
    }
}
