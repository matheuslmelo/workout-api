package com.matheus.workout_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheus.workout_api.builders.UserTestDataBuilder;
import com.matheus.workout_api.dto.CreateUserRequest;
import com.matheus.workout_api.dto.UpdateUserRequest;
import com.matheus.workout_api.entity.User;
import com.matheus.workout_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    private CreateUserRequest buildValidCreateUserRequest() {
        return new UserTestDataBuilder().buildCreateRequest();
    }

    private UpdateUserRequest buildValidUpdateUserRequest(){
        return new UserTestDataBuilder().buildUpdateRequest();
    }

    private User buildUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }


    @Nested
    @DisplayName("GET /users/{userId}")
    class GetUserByIdTest {

        @Test
        @DisplayName("Should return user by id")
        void shouldReturnUserById() throws Exception {
            User savedUser = userRepository.save(buildUser("Matheus", "matheus@gmail.com"));

            assertNotNull(savedUser.getId());

            mockMvc.perform(get("/users/{userId}", savedUser.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(savedUser.getId().toString()))
                    .andExpect(jsonPath("$.name").value("Matheus"))
                    .andExpect(jsonPath("$.email").value("matheus@gmail.com"));
        }

        @Test
        @DisplayName("Should return 404 when user not found")
        void shouldReturn404WhenUserNotFound() throws Exception {
            UUID nonExistingId = UUID.randomUUID();

            mockMvc.perform(get("/users/{userId}", nonExistingId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /users")
    class GetUsersTest {

        @Test
        @DisplayName("Should return all users")
        void shouldReturnAllUsers() throws Exception {
            userRepository.saveAll(List.of(
                    buildUser("Matheus", "matheus@gmail.com"),
                    buildUser("João", "joao@gmail.com")
            ));

            mockMvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].name").value("Matheus"))
                    .andExpect(jsonPath("$[0].email").value("matheus@gmail.com"))
                    .andExpect(jsonPath("$[1].name").value("João"))
                    .andExpect(jsonPath("$[1].email").value("joao@gmail.com"));
        }

        @Test
        @DisplayName("Should return empty list when there are no users")
        void shouldReturnEmptyListWhenThereAreNoUsers() throws Exception {

            mockMvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("POST /users")
    class CreateUserTest {

        @Test
        @DisplayName("Should create user")
        void shouldCreateUser() throws Exception {
            CreateUserRequest request = buildValidCreateUserRequest();

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.name").value(request.getName()))
                    .andExpect(jsonPath("$.email").value(request.getEmail()));
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {
            CreateUserRequest request = buildValidCreateUserRequest();
            request.setName("");
            request.setEmail("invalid-email");

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 409 when email already exists")
        void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {

            CreateUserRequest request = buildValidCreateUserRequest();

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.name").value(request.getName()))
                    .andExpect(jsonPath("$.email").value(request.getEmail()));

            CreateUserRequest duplicatedRequest = buildValidCreateUserRequest();

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(duplicatedRequest)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("PATCH /users/{userId}")
    class UpdateUserTest {

        @Test
        @DisplayName("Should update user")
        void shouldUpdateUser() throws Exception {
            User savedUser = userRepository.save(buildUser("Matheus", "matheus@gmail.com"));

            UpdateUserRequest request = buildValidUpdateUserRequest();
            request.setName("Matheus Melo");
            request.setAge(25);

            mockMvc.perform(patch("/users/{userId}", savedUser.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(savedUser.getId().toString()))
                    .andExpect(jsonPath("$.name").value("Matheus Melo"))
                    .andExpect(jsonPath("$.email").value("matheus@gmail.com"));
        }

        @Test
        @DisplayName("Should return 400 when update request is invalid")
        void shouldReturn400WhenUpdateRequestIsInvalid() throws Exception {
            User savedUser = userRepository.save(buildUser("Matheus", "matheus@gmail.com"));

            UpdateUserRequest request = new UpdateUserRequest();
            request.setName("");

            mockMvc.perform(patch("/users/{userId}", savedUser.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 when updating non-existing user")
        void shouldReturn404WhenUpdatingNonExistingUser() throws Exception {
            UpdateUserRequest request = new UpdateUserRequest();
            request.setName("Matheus Melo");
            request.setAge(25);

            mockMvc.perform(patch("/users/{userId}", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /users/{userId}")
    class DeleteUserTest {

        @Test
        @DisplayName("Should delete user")
        void shouldDeleteUser() throws Exception {
            User savedUser = userRepository.save(buildUser("Matheus", "matheus@gmail.com"));

            mockMvc.perform(delete("/users/{userId}", savedUser.getId()))
                    .andExpect(status().isNoContent());

            assertFalse(userRepository.findById(savedUser.getId()).isPresent());
        }

        @Test
        @DisplayName("Should return 404 when deleting non-existing user")
        void shouldReturn404WhenDeletingNonExistingUser() throws Exception {
            mockMvc.perform(delete("/users/{userId}", UUID.randomUUID()))
                    .andExpect(status().isNotFound());
        }
    }
}