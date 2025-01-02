package com.reonfernandes.Taskify.services;

import com.reonfernandes.Taskify.models.User;

import java.util.List;
import java.util.Optional;

public interface UserServices {
    // CRUD functionalities
    User saveUser (User user);
    List<User> getAllUsers();
    Optional<User> updateUser (User user);
    void deleteUser(String id);

    // Additional functionalities
    Optional<User> getUserById(String id);
    User getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);
}
