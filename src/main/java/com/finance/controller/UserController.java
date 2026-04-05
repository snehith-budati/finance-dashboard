package com.finance.controller;

import com.finance.entity.User;
import com.finance.service.AuthorizationService;
import com.finance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthorizationService authorizationService;

    public UserController(UserService userService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/bootstrap-admin")
    public ResponseEntity<User> bootstrapAdmin(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.bootstrapAdmin(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("X-User-Id") Long actorUserId) {
        authorizationService.requireAdmin(actorUserId);
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@RequestHeader("X-User-Id") Long actorUserId, @PathVariable Long id) {
        authorizationService.requireAdmin(actorUserId);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestHeader("X-User-Id") Long actorUserId, @Valid @RequestBody User user) {
        authorizationService.requireAdmin(actorUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @RequestHeader("X-User-Id") Long actorUserId,
            @PathVariable Long id,
            @Valid @RequestBody User updatedUser) {
        authorizationService.requireAdmin(actorUserId);
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@RequestHeader("X-User-Id") Long actorUserId, @PathVariable Long id) {
        authorizationService.requireAdmin(actorUserId);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
