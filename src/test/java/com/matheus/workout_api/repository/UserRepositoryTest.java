package com.matheus.workout_api.repository;

import com.matheus.workout_api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setName("Junko");
        testUser.setEmail("junko@gmail.com");
        testUser.setPassword("junko123pwd");
        testUser.setGoal("get stronger");
        testUser.setHeight(1.70);
        testUser.setWeight(80);
        testUser = userRepository.saveAndFlush(testUser);
    }

    @Test
    void shouldGenerateIdWhenSavingUser() {
        User user = new User();
        user.setName("Maria");
        user.setEmail("maria@gmail.com");
        user.setPassword("123456");
        user.setGoal("lose weight");
        user.setHeight(1.65);
        user.setWeight(60);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundById() {
        Optional<User> foundUser = userRepository.findById(testUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getName(), foundUser.get().getName());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
        assertEquals(testUser.getPassword(), foundUser.get().getPassword());
        assertEquals(testUser.getGoal(), foundUser.get().getGoal());
        assertEquals(testUser.getHeight(), foundUser.get().getHeight());
        assertEquals(testUser.getWeight(), foundUser.get().getWeight());
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        String email = "junko@gmail.com";
        boolean userExists = userRepository.existsByEmail(email);
        assertTrue(userExists);
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        boolean userExists = userRepository.existsByEmail("doesnotexist@gmail.com");
        assertFalse(userExists);
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundByEmail() {
        Optional<User> foundUser = userRepository.findByEmail(testUser.getEmail());

        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getName(), foundUser.get().getName());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
        assertEquals(testUser.getPassword(), foundUser.get().getPassword());
    }

    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<User> foundUser = userRepository.findByEmail("naoexiste@gmail.com");
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<User> foundUser = userRepository.findById(UUID.randomUUID());
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void shouldUpdateUserWhenSavingExistingUser() {
        testUser.setGoal("gain muscle");
        testUser.setWeight(78);

        User updatedUser = userRepository.save(testUser);

        assertEquals("gain muscle", updatedUser.getGoal());
        assertEquals(78, updatedUser.getWeight());

        User foundUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals("gain muscle", foundUser.getGoal());
        assertEquals(78, foundUser.getWeight());
    }

    @Test
    void shouldDeleteUser() {
        userRepository.delete(testUser);

        Optional<User> deletedUser = userRepository.findById(testUser.getId());
        assertTrue(deletedUser.isEmpty());
    }

}
