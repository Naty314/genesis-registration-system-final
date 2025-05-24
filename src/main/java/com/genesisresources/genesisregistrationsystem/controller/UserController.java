package com.genesisresources.genesisregistrationsystem.controller;

import com.genesisresources.genesisregistrationsystem.request.UserRequest;
import com.genesisresources.genesisregistrationsystem.response.UserResponse;
import com.genesisresources.genesisregistrationsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        logger.info("Vytvářím uživatele: {}", request);
        UserResponse createdUser = userService.createUser(request);
        logger.info("Uživatel vytvořen s ID: {}", createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean detail) {
        logger.info("Získávám uživatele s ID: {}, detail: {}", id, detail);
        UserResponse user = userService.getUserById(id, detail);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(defaultValue = "false") boolean detail) {
        logger.info("Získávám všechny uživatele, detail: {}", detail);
        List<UserResponse> users = userService.getAllUsers(detail);
        if (users.isEmpty()) {
            logger.info("Nebyli nalezeni žádní uživatelé.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest request) {
        logger.info("Aktualizuji uživatele: {}", request);
        UserResponse updatedUser = userService.updateUser(request);
        logger.info("Uživatel s ID {} aktualizován.", updatedUser.getId());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        logger.info("Mažu uživatele s ID: {}", id);
        userService.deleteUser(id);
        logger.info("Uživatel s ID {} byl smazán.", id);
        return ResponseEntity.ok("User with ID " + id + " has been deleted.");
    }
}