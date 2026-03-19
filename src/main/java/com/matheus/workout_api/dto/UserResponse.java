package com.matheus.workout_api.dto;

import java.util.UUID;

public class UserResponse {


        private UUID id;
        private String name;
        private String email;
        private int age;
        private String goal;
        private double height;
        private double weight;

        public UserResponse(){
        }

        public UserResponse(UUID id, String name, String email, int age, String goal, double height, double weight) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
            this.goal = goal;
            this.height = height;
            this.weight = weight;
        }

        public UUID getId () {
            return id;
        }

        public void setId (UUID id){
            this.id = id;
        }

        public String getName () {
            return name;
        }

        public void setName (String name){
            this.name = name;
        }

        public String getEmail () {
            return email;
        }

        public void setEmail (String email){
            this.email = email;
        }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGoal () {
            return goal;
        }

        public void setGoal (String goal){
            this.goal = goal;
        }

        public double getHeight () {
            return height;
        }

        public void setHeight ( double height){
            this.height = height;
        }

        public double getWeight () {
            return weight;
        }

        public void setWeight ( double weight){
            this.weight = weight;
        }
    }