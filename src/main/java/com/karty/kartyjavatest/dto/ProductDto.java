package com.karty.kartyjavatest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String category;
}
