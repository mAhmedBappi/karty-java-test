package com.karty.kartyjavatest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    public Product create(Product product) throws JsonProcessingException {
        product = productsRepository.save(product);
        jedis.jsonSet(createRedisKey(product.getId()), Utility.objectMapper().writeValueAsString(product));
        return product;
    }

    @Override
    public List<Object> retrieveAll() {
        return jedis.keys(REDIS_KEY_PREFIX + "*").stream().map(jedis::jsonGet).toList();
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
    public Product update(Product product, Long id) throws JsonProcessingException {
        if (!productsRepository.existsById(id)) {
            throw new IllegalArgumentException("Not Found");
        }

        product.setId(id);
        product = productsRepository.save(product);
        jedis.jsonSet(createRedisKey(product.getId()), Utility.objectMapper().writeValueAsString(product));
        return product;
    }

    @Override
    public boolean delete(Long id) {
        if (!productsRepository.existsById(id)) {
            throw new IllegalArgumentException("Not Found");
        }

        productsRepository.deleteById(id);
        jedis.jsonDel(createRedisKey(id));
        return true;
    }

    private String createRedisKey(Long id) {
        return REDIS_KEY_PREFIX + id;
    }
}
