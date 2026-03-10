package com.matheus.workout_api.entity;

import com.matheus.workout_api.enums.MuscleGroup;
import jakarta.persistence.*;

@Entity(name = "exercises")
public class Exercise {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "name")
   private String name;

   @Column(name = "description")
   private String description;

   @Enumerated(EnumType.STRING)
   @Column(name = "muscle_group")
   private MuscleGroup muscleGroup;

   public Exercise() {
   }

   public Exercise(String name, String description, MuscleGroup muscleGroup) {
      this.name = name;
      this.description = description;
      this.muscleGroup = muscleGroup;
   }

   public Long getId() {
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

   public MuscleGroup getMuscleGroup() {
      return muscleGroup;
   }

   public void setMuscleGroup(MuscleGroup muscleGroup) {
      this.muscleGroup = muscleGroup;
   }
}
