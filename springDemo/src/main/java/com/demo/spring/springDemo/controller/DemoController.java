package com.demo.spring.springDemo.controller;

import com.demo.spring.springDemo.model.Product;
import com.demo.spring.springDemo.view.ProductService;
import com.demo.spring.springDemo.model.User;
import com.demo.spring.springDemo.view.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*; // Change Controller to RestController
import java.util.List;

@RestController // <-- Correction 1
public class DemoController {
    @Autowired
    ProductService productService;
    @Autowired // <-- Correction 2
    UserService userService;

    // ... (no changes to the product methods) ...
    @GetMapping("/products")
    public List<Product> getProducts(){
        return productService.getProducts();
    }
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable int id){
        return productService.getProduct(id);
    }
    @PostMapping("/addProduct")
    public boolean addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }
    @PutMapping("/product/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product product){
        return  productService.updateProduct(id, product);
    }
    @DeleteMapping("/product/{id}")
    public String deleteProduct(@PathVariable int id){
        return productService.deleteProduct(id);
    }
    @GetMapping("/product/findByName")
    public List<Product> findByName(@RequestParam String name){
        return productService.findByName(name);
    }

    // ... (user methods)
    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable int id){
        return userService.getUser(id);
    }
    @PostMapping("/addUser")
    public boolean addUser(@RequestBody User user){
        return userService.addUser(user);
    }
    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user){
        return  userService.updateUser(id, user);
    }
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable int id){
        return userService.deleteUser(id);
    }

    @GetMapping("/user/findByName")
    public List<User> findUserByName(@RequestParam String name) { // <-- Correction 3
        return userService.findByName(name);
    }
}