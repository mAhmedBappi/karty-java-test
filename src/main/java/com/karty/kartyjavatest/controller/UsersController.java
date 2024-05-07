package com.karty.kartyjavatest.controller;

import com.karty.kartyjavatest.model.User;
import com.karty.kartyjavatest.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@Valid @RequestBody User user) {
        return ResponseEntity.ok().body(this.usersService.create(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody User user) {
        return this.usersService.login(user);
    }

    @GetMapping("")
    public ResponseEntity<Object> retrieveAllUsers() {
        return ResponseEntity.ok().body(this.usersService.retrieveAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.usersService.delete(id));
    }
}
