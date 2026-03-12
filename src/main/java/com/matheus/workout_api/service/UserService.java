package com.matheus.workout_api.service;

import com.matheus.workout_api.entity.User;
import com.matheus.workout_api.exception.EmailAlreadyExistsException;
import com.matheus.workout_api.exception.UserNotFoundException;
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
    public User createUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistsException();
        }
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
    public User updateUser(UUID userId, User updatedUser){
        User user = this.getUserById(userId);
        if (!user.getEmail().equals(updatedUser.getEmail())
                && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        user.setEmail(updatedUser.getEmail());
        user.setName(updatedUser.getName());
        user.setPassword(updatedUser.getPassword());
        user.setHeight(updatedUser.getHeight());
        user.setWeight(updatedUser.getWeight());
        user.setGoal(updatedUser.getGoal());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID userId){
        User user = this.getUserById(userId);
        userRepository.delete(user);
    }
}
