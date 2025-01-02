package com.reonfernandes.Taskify.services.impl;

import com.reonfernandes.Taskify.exceptions.ResourceNotFoundException;
import com.reonfernandes.Taskify.models.User;
import com.reonfernandes.Taskify.repositories.UserRepository;
import com.reonfernandes.Taskify.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.lang.String;

@Service
public class UserServicesImpl implements UserServices {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServicesImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        logger.info("Saving user: {}", user);

        // Randomly generate ID.
        String userId = UUID.randomUUID().toString();
        user.setId(userId);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the repository
        User savedUser = userRepository.save(user);

        logger.info("User saved with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> updateUser(User user) {
        // Check whether the user exists of not
        User newUser = userRepository.findById(user.getId()).orElseThrow(
                ()-> new ResourceNotFoundException("User with id: " + user.getId() + " not found."));

        logger.info("Updating user: {}", user);

        newUser.setName(user.getName());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setPassword(user.getPassword());
        newUser.setProfilePic(user.getProfilePic());

        newUser.setAccountEnabled(user.isAccountEnabled());
        newUser.setEmailVerified(user.isEmailVerified());

        newUser.setProviders(user.getProviders());
        newUser.setProviderId(user.getProviderId());

        User savedUser = userRepository.save(newUser);
        logger.info("Updated user: {}", savedUser);

        return Optional.of(savedUser);
    }

    @Override
    public void deleteUser(String id) {
        User deleteUser = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User with id: "+ id + " not found")
        );
        logger.info("User with id: {}", deleteUser);
        userRepository.delete(deleteUser);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
