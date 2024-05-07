package com.karty.kartyjavatest.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthRequest {
    private String username;
    private String password;
}