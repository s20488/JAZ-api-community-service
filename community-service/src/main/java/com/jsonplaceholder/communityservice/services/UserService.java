package com.jsonplaceholder.communityservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jsonplaceholder.communityservice.model.User;
import com.jsonplaceholder.communityservice.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        logger.info("Saving user: {}", user);
        userRepository.save(user);
    }

    public ResponseEntity<List<User>> fetchAllUsers() {
        logger.info("Fetching all users");
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<Optional<User>> fetchUserById(Integer id) {
        logger.info("Fetching user by ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.info("User found: {}", user.get());
            return ResponseEntity.ok(user);
        } else {
            logger.warn("User not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> updateUserById(Integer id, User updatedUser) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        logger.info("Updating user by ID: {}", id);
        User existingUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));

        if (updatedUser.getId() != null) {
            return ResponseEntity.badRequest().body("You can't change the user id. Please try again");
        }
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getAddress() != null) {
            existingUser.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getPhone() != null) {
            existingUser.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getWebsite() != null) {
            existingUser.setWebsite(updatedUser.getWebsite());
        }
        if (updatedUser.getCompany() != null) {
            existingUser.setCompany(updatedUser.getCompany());
        }

        User savedEntity = userRepository.save(existingUser);
        logger.info("User updated successfully: {}", savedEntity);

        return ResponseEntity.ok(savedEntity);
    }

    public void deleteUserById(Integer id) {
        logger.info("Deleting user by ID: {}", id);
        userRepository.deleteById(id);
    }

    public User findUserByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }
}
