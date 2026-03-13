package com.matheus.workout_api.service;

import com.matheus.workout_api.builders.UserTestDataBuilder;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {

        userId = UUID.randomUUID();
        user = new UserTestDataBuilder().build();
    }

    private User buildUser(String name, String email) {
        return new UserTestDataBuilder()
                .withName(name)
                .withEmail(email)
                .build();
    }

    @Nested
    class GetByUserIdTest {

        @Test
        @DisplayName("Should return an user when id is valid")
        public void shouldReturnUserWhenUserIdIsValid() {

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            User result = userService.getUserById(userId);

            assertNotNull(result);
            assertEquals("Matheus", result.getName());
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when id is invalid on getting an user")
        public void shouldThrowExceptionWhenUserIdIsInvalid() {

            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
            verify(userRepository).findById(userId);
        }
    }


    @Nested
    class CreateUserTest{

        @Test
        @DisplayName("Should throw EmailAlreadyExistsException when email is already registered on creating a new user")
        public void shouldThrowExceptionWhenEmailAlreadyExistsOnCreateUser() {

            when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(user));
            verify(userRepository).existsByEmail(user.getEmail());
        }

        @Test
        @DisplayName("Should save a new user when email is not registered yet")
        public void shouldSaveUserWhenEmailIsNew() {

            when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
            when(userRepository.save(user)).thenReturn(user);

            User result = userService.createUser(user);

            assertNotNull(result);
            assertEquals(user.getEmail(),result.getEmail());
            verify(userRepository).existsByEmail(user.getEmail());
            verify(userRepository).save(user);
        }
    }


    @Nested
    class GetAllUsersTest{

        @Test
        @DisplayName("Should retrieve all users if they exist")
        public void shouldReturnAllUsers() {

            User anotherUser = buildUser("joão","joao@gmail.com");
            List<User> users = List.of(user,anotherUser);
            when(userRepository.findAll()).thenReturn(users);

            List<User> result = userService.getAllUsers();

            assertNotNull(result);
            assertEquals(2,result.size());
            verify(userRepository).findAll();
        }

        @Test
        @DisplayName("Should return an empty list of users if there are no users on database")
        public void shouldReturnEmptyListWhenNoUsers() {

            when(userRepository.findAll()).thenReturn(Collections.emptyList());

            List<User> result = userService.getAllUsers();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(userRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Should update an user if email did not change")
    class UpdateUserTest{

        @Test
        public void shouldUpdateUserWhenEmailDidNotChange(){

            User updatedUser = buildUser("Matheus Melo","matheus@gmail.com");
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(user)).thenReturn(user);

            User result = userService.updateUser(userId,updatedUser);

            assertNotNull(result);
            assertEquals("Matheus Melo", result.getName());
            assertEquals(user.getEmail(), result.getEmail());
            verify(userRepository).findById(userId);
            verify(userRepository).save(user);
            verify(userRepository,never()).existsByEmail(anyString());
        }

        @Test
        @DisplayName("Should update an user if email changed but it is currently available")
        public void shouldUpdateUserWhenEmailChangedAndAvailable(){

            User existingUser = user;
            User updatedUser = buildUser("Matheus Melo","melomath@gmail.com");
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(userRepository.existsByEmail(updatedUser.getEmail())).thenReturn(false);
            when(userRepository.save(existingUser)).thenReturn(existingUser);

            User result = userService.updateUser(userId,updatedUser);

            assertNotNull(result);
            assertEquals(updatedUser.getEmail(),result.getEmail());
            verify(userRepository).findById(userId);
            verify(userRepository).existsByEmail(updatedUser.getEmail());
            verify(userRepository).save(existingUser);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException if userId is invalid on update")
        public void shouldThrowExceptionWhenUserIdIsInvalidOnUpdate(){

            User updatedUser = buildUser("Matheus Melo","matheus@gmail.com");
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId,updatedUser));

            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw EmailAlreadyExistsException if userId is invalid on update")
        public void shouldThrowExceptionWhenEmailAlreadyExistsOnUpdate(){
            User existingUser = buildUser("Matheus","old@gmail.com");
            User updatedUser = buildUser("Matheus Melo","matheus@gmail.com");
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(userRepository.existsByEmail(updatedUser.getEmail())).thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> userService.updateUser(userId,updatedUser));
            verify(userRepository).findById(userId);
            verify(userRepository).existsByEmail(updatedUser.getEmail());
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Should delete a valid user sucessfully")
    class DeleteUserTest{

        @Test
        public void shouldDeleteUserWhenUserExists(){

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            userService.deleteUser(userId);

            verify(userRepository).findById(userId);
            verify(userRepository).delete(user);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException if userId is invalid on delete")
        public void shouldThrowExceptionWhenUserNotFoundOnDelete(){

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> userService.deleteUser(userId));

            verify(userRepository).findById(userId);
            verify(userRepository, never()).delete(any());
        }
    }

}