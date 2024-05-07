package com.karty.kartyjavatest.repository;

import com.karty.kartyjavatest.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
}
