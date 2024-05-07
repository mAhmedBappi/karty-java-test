package com.karty.kartyjavatest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.karty.kartyjavatest.model.Product;

import java.util.List;

public interface ProductsService {
    Product create(Product product) throws JsonProcessingException;

    List<Object> retrieveAll();

    Product retrieveById(Long id) throws JsonProcessingException;

    Product update(Product product, Long id) throws JsonProcessingException;

    boolean delete(Long id);
}
