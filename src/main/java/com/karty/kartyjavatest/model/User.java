package com.karty.kartyjavatest.model;

import com.karty.kartyjavatest.dto.UserDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user", indexes = {@Index(columnList = "username", name = "idx_username", unique = true)})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;

    public User(UserDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
    }
}
