package com.matheus.workout_api.entity;

import com.matheus.workout_api.enums.DifficultyLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "workout_exercises")
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "series_planned")
    private int seriesPlanned;

    @Column(name = "min_reps")
    private int minReps;

    @Column(name = "max_reps")
    private int maxReps;

    @Column(name = "suggested_load_kg")
    private double suggestedLoad;

    @Column(name = "load_adjustment_kg")
    private double loadAdjustment;

    @Column(name = "min_reps_achieved")
    private int minRepsAchieved;

    @Column(name = "used_load_kg")
    private double usedLoad;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level")
    private DifficultyLevel difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    public WorkoutExercise() {
    }

    public WorkoutExercise(int seriesPlanned, int minReps, int maxReps, double suggestedLoad, double loadAdjustment, int minRepsAchieved, double usedLoad, Workout workout, Exercise exercise, DifficultyLevel difficultyLevel) {
        this.seriesPlanned = seriesPlanned;
        this.minReps = minReps;
        this.maxReps = maxReps;
        this.suggestedLoad = suggestedLoad;
        this.loadAdjustment = loadAdjustment;
        this.minRepsAchieved = minRepsAchieved;
        this.usedLoad = usedLoad;
        this.workout = workout;
        this.exercise = exercise;
        this.difficultyLevel = difficultyLevel;
    }

    public Long getId() {
        return id;
    }

    public int getSeriesPlanned() {
        return seriesPlanned;
    }

    public void setSeriesPlanned(int seriesPlanned) {
        this.seriesPlanned = seriesPlanned;
    }

    public int getMinReps() {
        return minReps;
    }

    public void setMinReps(int minReps) {
        this.minReps = minReps;
    }

    public int getMaxReps() {
        return maxReps;
    }

    public void setMaxReps(int maxReps) {
        this.maxReps = maxReps;
    }

    public double getSuggestedLoad() {
        return suggestedLoad;
    }

    public void setSuggestedLoad(double suggestedLoad) {
        this.suggestedLoad = suggestedLoad;
    }

    public double getLoadAdjustment() {
        return loadAdjustment;
    }

    public void setLoadAdjustment(double loadAdjustment) {
        this.loadAdjustment = loadAdjustment;
    }

    public int getMinRepsAchieved() {
        return minRepsAchieved;
    }

    public void setMinRepsAchieved(int minRepsAchieved) {
        this.minRepsAchieved = minRepsAchieved;
    }

    public double getUsedLoad() {
        return usedLoad;
    }

    public void setUsedLoad(double usedLoad) {
        this.usedLoad = usedLoad;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}