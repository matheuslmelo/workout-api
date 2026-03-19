package com.matheus.workout_api.service;

import com.matheus.workout_api.builders.UserTestDataBuilder;
import com.matheus.workout_api.dto.CreateUserRequest;
import com.matheus.workout_api.dto.UpdateUserRequest;
import com.matheus.workout_api.entity.User;
import com.matheus.workout_api.exception.EmailAlreadyExistsException;
import com.matheus.workout_api.exception.UserNotFoundException;
import com.matheus.workout_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new UserTestDataBuilder().build();
        createUserRequest = new UserTestDataBuilder().buildCreateRequest();
        updateUserRequest = new UserTestDataBuilder().buildUpdateRequest();
    }

    private User buildUser(String name, String email) {
        return new UserTestDataBuilder()
                .withName(name)
                .withEmail(email)
                .build();
    }

    @Nested
    @DisplayName("Get user by id")
    class GetByUserIdTest {

        @Test
        @DisplayName("Should return a user when id is valid")
        void shouldReturnUserWhenUserIdIsValid() {
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            User result = userService.getUserById(userId);

            assertNotNull(result);
            assertEquals(user.getName(), result.getName());
            assertEquals(user.getEmail(), result.getEmail());

            verify(userRepository).findById(userId);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when id is invalid")
        void shouldThrowExceptionWhenUserIdIsInvalid() {
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));

            verify(userRepository).findById(userId);
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Nested
    @DisplayName("Create user")
    class CreateUserTest {

        @Test
        @DisplayName("Should throw EmailAlreadyExistsException when email is already registered")
        void shouldThrowExceptionWhenEmailAlreadyExistsOnCreateUser() {
            when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(createUserRequest));

            verify(userRepository).existsByEmail(createUserRequest.getEmail());
            verify(userRepository, never()).save(any(User.class));
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("Should save a new user when email is not registered yet")
        void shouldSaveUserWhenEmailIsNew() {
            when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(user);

            User result = userService.createUser(createUserRequest);

            assertNotNull(result);
            assertEquals(user.getName(), result.getName());
            assertEquals(user.getEmail(), result.getEmail());

            verify(userRepository).existsByEmail(createUserRequest.getEmail());
            verify(userRepository).save(any(User.class));
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Nested
    @DisplayName("Get all users")
    class GetAllUsersTest {

        @Test
        @DisplayName("Should retrieve all users if they exist")
        void shouldReturnAllUsers() {
            User anotherUser = buildUser("João", "joao@gmail.com");
            List<User> users = List.of(user, anotherUser);

            when(userRepository.findAll()).thenReturn(users);

            List<User> result = userService.getAllUsers();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(users, result);

            verify(userRepository).findAll();
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("Should return an empty list when there are no users")
        void shouldReturnEmptyListWhenNoUsers() {
            when(userRepository.findAll()).thenReturn(Collections.emptyList());

            List<User> result = userService.getAllUsers();

            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(userRepository).findAll();
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Nested
    @DisplayName("Update user")
    class UpdateUserTest {

        @Test
        @DisplayName("Should update a user successfully")
        void shouldUpdateUser() {
            User existingUser = buildUser("Matheus", "matheus@gmail.com");

            UpdateUserRequest request = new UpdateUserRequest();
            request.setName("Matheus Melo");

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(userRepository.save(existingUser)).thenReturn(existingUser);

            User result = userService.updateUser(userId, request);

            assertNotNull(result);
            assertEquals("Matheus Melo", result.getName());
            assertEquals("matheus@gmail.com", result.getEmail());

            verify(userRepository).findById(userId);
            verify(userRepository).save(existingUser);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user id is invalid on update")
        void shouldThrowExceptionWhenUserIdIsInvalidOnUpdate() {
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, updateUserRequest));

            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any());
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Nested
    @DisplayName("Delete user")
    class DeleteUserTest {

        @Test
        @DisplayName("Should delete a user when it exists")
        void shouldDeleteUserWhenUserExists() {
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            userService.deleteUser(userId);

            verify(userRepository).findById(userId);
            verify(userRepository).delete(user);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user id is invalid on delete")
        void shouldThrowExceptionWhenUserNotFoundOnDelete() {
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));

            verify(userRepository).findById(userId);
            verify(userRepository, never()).delete(any());
            verifyNoMoreInteractions(userRepository);
        }
    }
}