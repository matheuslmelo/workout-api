package com.matheus.workout_api.controller;

import com.matheus.workout_api.dto.CreateUserRequest;
import com.matheus.workout_api.dto.UpdateUserRequest;
import com.matheus.workout_api.dto.UserResponse;
import com.matheus.workout_api.entity.User;
import com.matheus.workout_api.mapper.UserMapper;
import com.matheus.workout_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId){
        User user = userService.getUserById(userId);
        return  ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(UserMapper.toResponseList(users));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request){
        User savedUser = userService.createUser(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponse(savedUser));
    }

    @PatchMapping   ("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId, @RequestBody @Valid UpdateUserRequest request){
        User updatedUser = userService.updateUser(userId, request);
        return  ResponseEntity.ok(UserMapper.toResponse(updatedUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}