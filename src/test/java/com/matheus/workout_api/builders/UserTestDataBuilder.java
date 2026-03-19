package com.matheus.workout_api.builders;

import com.matheus.workout_api.dto.CreateUserRequest;
import com.matheus.workout_api.dto.UpdateUserRequest;
import com.matheus.workout_api.entity.User;

public class UserTestDataBuilder {

    private String name = "Matheus";
    private String email = "matheus@gmail.com";
    private int age = 25;
    private String password = "123456";
    private String goal = "Lose weight";
    private double height = 1.70;
    private double weight = 90;

    public UserTestDataBuilder withName(String name){
        this.name = name;
        return this;
    }

    public UserTestDataBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public User build() {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setPassword(password);
        user.setGoal(goal);
        user.setHeight(height);
        user.setWeight(weight);
        return user;
    }

    public CreateUserRequest buildCreateRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName(name);
        request.setEmail(email);
        request.setAge(age);
        request.setPassword(password);
        request.setGoal(goal);
        request.setHeight(height);
        request.setWeight(weight);
        return request;
    }

    public UpdateUserRequest buildUpdateRequest() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName(name);
        request.setPassword(password);
        request.setGoal(goal);
        request.setHeight(height);
        request.setWeight(weight);
        return request;
    }
}