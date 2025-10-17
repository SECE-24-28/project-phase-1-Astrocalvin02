package com.demo.spring.springDemo.repository;

import com.demo.spring.springDemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findByName(String name);
    List<Product> findByPrice(Double price);
    List<Product> findByNameAndPrice(String name, Double price);
    List<Product> findByPriceGreaterThan(Double price);
    List<Product> findByPriceLessThan(Double price);
}
