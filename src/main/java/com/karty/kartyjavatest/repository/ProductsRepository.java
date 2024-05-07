package com.karty.kartyjavatest.repository;

import com.karty.kartyjavatest.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductsRepository extends CrudRepository<Product, Long> {
    @Override
    @Modifying
    @Query("update Product product set product.deleted = true where product.id = ?1")
    void deleteById(Long id);

    @Override
    @Query("select product from Product product where product.id = ?1 and product.deleted = false")
    Optional<Product> findById(Long id);

    @Override
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.id = ?1 AND p.deleted = false")
    boolean existsById(Long id);
}
