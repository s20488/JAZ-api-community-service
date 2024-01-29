package com.jsonplaceholder.communityservice.controllers;

import com.jsonplaceholder.communityservice.model.User;
import com.jsonplaceholder.communityservice.client.RestClient;
import com.jsonplaceholder.communityservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final RestClient externalUserService;

    public UserController(UserService userService, RestClient externalUserService) {
        this.userService = userService;
        this.externalUserService = externalUserService;
    }

    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        user.setId(null); // Zresetowanie identyfikatora, aby baza danych wygenerowała nowy
        userService.saveUser(user);
        return ResponseEntity.ok("User has been successfully added. Please update the list with: http://localhost:8080/api/v1/users");
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.fetchAllUsers().getBody();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Integer id, Model model) {
        ResponseEntity<Optional<User>> userResponse = userService.fetchUserById(id);
        User user = userResponse.getBody().get();
        model.addAttribute("users", user);
        return "users";
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable Integer id, @RequestBody User user) {
        ResponseEntity<?> updateUser = userService.updateUserById(id, user);
        if (updateUser.getStatusCode().is2xxSuccessful()) {
            // Jeśli status HTTP jest udany (w zakresie od 200 do 299)
            return ResponseEntity.ok("User data with id: " + id + " has been successfully updated. Please update the list with: http://localhost:8080/api/v1/users");
        } else {
            return ResponseEntity.status(updateUser.getStatusCode()).body(updateUser.getBody().toString());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User data with id: " + id + " has been successfully deleted. Please update the list with: http://localhost:8080/api/v1/users");
    }

    @GetMapping("/findUserByUsername/{username}")
    public String findUserByUsername(@PathVariable String username, Model model) {
        User user = userService.findUserByUsername(username);
        if (user != null) {
            model.addAttribute("users", Collections.singletonList(user));
        } else {
            model.addAttribute("users", Collections.emptyList());
        }
        return "users";
    }

    @GetMapping("/external")
    public ResponseEntity<String> getExternalUsers() {
        User[] users = externalUserService.fetchExternalUsers();
        for (User user : users) {
            userService.saveUser(user);
        }
        return ResponseEntity.ok("The data has been successfully migrated. Please update the list with: http://localhost:8080/api/v1/users");
    }

    @GetMapping("/external/{id}")
    public ResponseEntity<String> getExternalUserById(@PathVariable Integer id) {
        User user = externalUserService.fetchExternalUserById(id);
        userService.saveUser(user);
        return ResponseEntity.ok("User data with id: " + id + " has been successfully migrated. Please update the list with: http://localhost:8080/api/v1/users");
    }
}
