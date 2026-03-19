package com.matheus.workout_api.mapper;

import com.matheus.workout_api.dto.CreateUserRequest;
import com.matheus.workout_api.dto.UpdateUserRequest;
import com.matheus.workout_api.dto.UserResponse;
import com.matheus.workout_api.entity.User;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntity(CreateUserRequest dto){
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(20);
        user.setPassword(dto.getPassword());
        user.setGoal(dto.getGoal());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());
        return user;
    }

    public static User toEntity(UpdateUserRequest dto){
        User user = new User();
        user.setName(dto.getName());
        user.setAge(dto.getAge());
        user.setPassword(dto.getPassword());
        user.setGoal(dto.getGoal());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());
        return user;
    }

    public static UserResponse toResponse(User user){
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setGoal(user.getGoal());
        dto.setHeight(user.getHeight());
        dto.setWeight(user.getWeight());
        return dto;
    }

    public static List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

}