package com.karty.kartyjavatest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
