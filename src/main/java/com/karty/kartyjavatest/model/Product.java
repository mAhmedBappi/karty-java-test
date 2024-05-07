package com.karty.kartyjavatest.model;

import com.karty.kartyjavatest.dto.ProductDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String category;
    private boolean deleted;

    public Product(ProductDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.category = dto.getCategory();
    }

    public void update(ProductDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.category = dto.getCategory();
    }
}
