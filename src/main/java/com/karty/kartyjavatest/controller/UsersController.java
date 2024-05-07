package com.karty.kartyjavatest.controller;

import com.karty.kartyjavatest.dto.DeleteResponse;
import com.karty.kartyjavatest.dto.UserDto;
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
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok().body(this.usersService.create(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UserDto dto) {
        return this.usersService.login(dto);
    }

    @GetMapping("")
    public ResponseEntity<Object> retrieveAllUsers() {
        return ResponseEntity.ok().body(this.usersService.retrieveAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        this.usersService.delete(id);
        return ResponseEntity.ok().body(new DeleteResponse("User " + id + " deleted successfully"));
    }
}
