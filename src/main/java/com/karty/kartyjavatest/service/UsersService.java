package com.karty.kartyjavatest.service;

import com.karty.kartyjavatest.dto.UserDto;
import com.karty.kartyjavatest.model.User;
import org.springframework.http.ResponseEntity;

public interface UsersService {
    User create(UserDto dto);

    ResponseEntity<Object> login(UserDto dto);

    Iterable<User> retrieveAll();

    User retrieveById(Long id);

    void delete(Long id);
}
