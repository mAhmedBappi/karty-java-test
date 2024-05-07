package com.karty.kartyjavatest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.karty.kartyjavatest.dto.ProductDto;
import com.karty.kartyjavatest.model.Product;

import java.util.List;

public interface ProductsService {
    Product create(ProductDto dto) throws JsonProcessingException;

    List<Product> retrieveAll();

    List<Product> retrieveAllByNameAndDescription(String name, String description);

    List<Product> retrieveAllByName(String name);

    List<Product> retrieveAllByDescription(String description);

    Product retrieveById(Long id) throws JsonProcessingException;

    Product update(ProductDto dto, Long id) throws JsonProcessingException;

    void delete(Long id);
}
