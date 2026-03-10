package com.matheus.workout_api.entity;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.util.UUID;

@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_active")
    private boolean active;

    public Workout() {
    }

    public Workout(String name, String description, DayOfWeek dayOfWeek, User user, boolean active) {
        this.name = name;
        this.description = description;
        this.dayOfWeek = dayOfWeek;
        this.user = user;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
