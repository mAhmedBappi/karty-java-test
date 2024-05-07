package com.karty.kartyjavatest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.karty.kartyjavatest.config.RateLimiterConfig;
import com.karty.kartyjavatest.dto.DeleteResponse;
import com.karty.kartyjavatest.dto.ProductDto;
import com.karty.kartyjavatest.exceptions.NotFoundException;
import com.karty.kartyjavatest.model.Product;
import com.karty.kartyjavatest.service.ProductsService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService productsService;
    private final Bucket bucket;

    @Autowired
    public ProductsController(ProductsService productsService, RateLimiterConfig config) {
        this.productsService = productsService;

        Bandwidth limit = Bandwidth.classic(config.getBucketSize(), Refill.greedy(config.getBucketSize(),
                Duration.ofSeconds(config.getRefillDuration())));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@Valid @RequestBody ProductDto product) throws JsonProcessingException {
        // Rate Limiting - 1 API call per 1 minute
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok().body(this.productsService.create(product));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @GetMapping("")
    public ResponseEntity<Object> retrieveAll(@RequestParam Optional<String> name, @RequestParam Optional<String> description) {
        if (name.isPresent() && description.isPresent()) {
            return ResponseEntity.ok().body(this.productsService.retrieveAllByNameAndDescription(name.get(), description.get()));
        } else if (name.isPresent()) {
            return ResponseEntity.ok().body(this.productsService.retrieveAllByName(name.get()));
        } else if (description.isPresent()) {
            return ResponseEntity.ok().body(this.productsService.retrieveAllByDescription(description.get()));
        } else {
            return ResponseEntity.ok().body(this.productsService.retrieveAll());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> retrieveById(@PathVariable Long id) throws JsonProcessingException {
        Product product = this.productsService.retrieveById(id);

        if (product == null) {
            throw new NotFoundException("No Product Found With Id: " + id);
        }

        return ResponseEntity.ok().body(this.productsService.retrieveById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody ProductDto dto, @PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok().body(this.productsService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        this.productsService.delete(id);
        return ResponseEntity.ok().body(new DeleteResponse("Product " + id + " deleted successfully"));
    }
}
