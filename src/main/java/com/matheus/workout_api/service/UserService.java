package com.matheus.workout_api.service;

import com.matheus.workout_api.dto.CreateUserRequest;
import com.matheus.workout_api.dto.UpdateUserRequest;
import com.matheus.workout_api.entity.User;
import com.matheus.workout_api.exception.EmailAlreadyExistsException;
import com.matheus.workout_api.exception.UserNotFoundException;
import com.matheus.workout_api.mapper.UserMapper;
import com.matheus.workout_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(CreateUserRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException();
        }
        User user = UserMapper.toEntity(request);
        return userRepository.save(user);
    }

    public User getUserById(UUID userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(UUID userId, UpdateUserRequest request){
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (request.getName() != null) {
            existingUser.setName(request.getName());
        }

        if (request.getGoal() != null) {
            existingUser.setGoal(request.getGoal());
        }

        if (request.getHeight() != null) {
            existingUser.setHeight(request.getHeight());
        }

        if (request.getWeight() != null) {
            existingUser.setWeight(request.getWeight());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existingUser.setPassword(request.getPassword());
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(UUID userId){
        User user = this.getUserById(userId);
        userRepository.delete(user);
    }
}
