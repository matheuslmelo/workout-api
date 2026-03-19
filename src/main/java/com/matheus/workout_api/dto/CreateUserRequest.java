package com.matheus.workout_api.dto;

import jakarta.validation.constraints.*;

public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must be at most 150 characters")
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 15, message = "Age must be at least 15")
    @Max(value = 70, message = "Age must be at most 70")
    private int age;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;

    @NotBlank(message = "Goal is required")
    @Size(min = 3, max = 120, message = "Goal must be between 3 and 120 characters")
    private String goal;

    @NotNull(message = "Height is required")
    @DecimalMin(value = "0.50", message = "Height must be at least 0.50")
    @DecimalMax(value = "2.80", message = "Height must be at most 2.80")
    private Double height;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "30.0", message = "Weight must be at least 20.0")
    @DecimalMax(value = "300.0", message = "Weight must be at most 500.0")
    private Double weight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim().toLowerCase() : null;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal != null ? goal.trim() : null;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}
