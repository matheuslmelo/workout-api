package com.matheus.workout_api.repository;

import com.matheus.workout_api.entity.Exercise;
import com.matheus.workout_api.enums.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByMuscleGroup(MuscleGroup muscleGroup);
    Optional<Exercise> findByName(String name);
}
