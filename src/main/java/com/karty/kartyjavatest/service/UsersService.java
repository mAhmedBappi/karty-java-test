package com.karty.kartyjavatest.service;

import com.karty.kartyjavatest.model.User;
import org.springframework.http.ResponseEntity;

public interface UsersService {
    User create(User user);

    ResponseEntity<Object> login(User user);

    Iterable<User> retrieveAll();

    User retrieveById(Long id);

    boolean delete(Long id);
}
