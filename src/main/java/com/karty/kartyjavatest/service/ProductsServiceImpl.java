package com.karty.kartyjavatest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.karty.kartyjavatest.dto.ProductDto;
import com.karty.kartyjavatest.model.Product;
import com.karty.kartyjavatest.repository.ProductsRepository;
import com.karty.kartyjavatest.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.util.List;

@Service
public class ProductsServiceImpl implements ProductsService {
    private static final Logger logger = LoggerFactory.getLogger(ProductsServiceImpl.class);
    private static final String REDIS_KEY_PREFIX = "product:";
    private final ProductsRepository productsRepository;
    private final JedisPooled jedis;

    @Autowired
    public ProductsServiceImpl(ProductsRepository productsRepository, JedisPooled jedis) {
        this.productsRepository = productsRepository;
        this.jedis = jedis;
    }

    @Override
    public Product create(ProductDto dto) throws JsonProcessingException {
        Product product = productsRepository.save(new Product(dto));
        jedis.jsonSet(createRedisKey(product.getId()), Utility.objectMapper().writeValueAsString(product));
        return product;
    }

    @Override
    public List<Product> retrieveAll() {
        return jedis.keys(REDIS_KEY_PREFIX + "*").stream()
                .map(jedis::jsonGet)
                .map(p -> Utility.objectMapper().convertValue(p, Product.class))
                .toList();
    }

    @Override
    public List<Product> retrieveAllByNameAndDescription(String name, String description) {
        return retrieveAll().stream().filter(p -> p.getName().equals(name) && p.getDescription().equals(description))
                .toList();
    }

    @Override
    public List<Product> retrieveAllByName(String name) {
        return retrieveAll().stream().filter(p -> p.getName().equals(name)).toList();
    }

    @Override
    public List<Product> retrieveAllByDescription(String description) {
        return retrieveAll().stream().filter(p -> p.getDescription().equals(description)).toList();
    }

    @Override
    public Product retrieveById(Long id) throws JsonProcessingException {
        logger.info("retrieve by id started");
        Product product = Utility.objectMapper().convertValue(jedis.jsonGet(createRedisKey(id)), Product.class);

        if (product == null) {
            logger.info("Product null in cache");
            product = productsRepository.findById(id).orElse(null);

            if (product != null) {
                jedis.jsonSet(createRedisKey(product.getId()), Utility.objectMapper().writeValueAsString(product));
            }
        }

        return product;
    }

    @Override
    public Product update(ProductDto dto, Long id) throws JsonProcessingException {
        Product product = productsRepository.findById(id).orElse(null);

        if (product == null) {
            throw new IllegalArgumentException("Product: " + id + " not found");
        }

        product.setId(id);
        product.update(dto);

        product = productsRepository.save(product);
        jedis.jsonSet(createRedisKey(product.getId()), Utility.objectMapper().writeValueAsString(product));

        return product;
    }

    @Override
    public void delete(Long id) {
        if (!productsRepository.existsById(id)) {
            throw new IllegalArgumentException("Not Found");
        }

        productsRepository.deleteById(id);
        jedis.jsonDel(createRedisKey(id));
    }

    private String createRedisKey(Long id) {
        return REDIS_KEY_PREFIX + id;
    }
}
