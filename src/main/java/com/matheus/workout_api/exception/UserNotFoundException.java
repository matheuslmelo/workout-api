package com.matheus.workout_api.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
      super("User not found for given id: "+userId);
    }
}
