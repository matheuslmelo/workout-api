package com.matheus.workout_api.builders;

import com.matheus.workout_api.entity.User;

public class UserTestDataBuilder {

    private String name = "Matheus";
    private String email = "matheus@gmail.com";
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
        user.setPassword(password);
        user.setGoal(goal);
        user.setHeight(height);
        user.setWeight(weight);
        return user;
    }
}