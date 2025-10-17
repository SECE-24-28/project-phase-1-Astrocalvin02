package com.demo.spring.springDemo.controller;

import com.demo.spring.springDemo.repository.ProductRepo;
import com.demo.spring.springDemo.repository.UserRepo; // 1. Import UserRepo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @Autowired
    ProductRepo productRepo;

    @Autowired // 2. Autowire the UserRepo
    UserRepo userRepo;

    @GetMapping("/")
    public String homePage(){
        return "Hello"; // Renders Hello.html
    }

    // This method serves productlist.html
    @GetMapping("/products-ui")
    public String displayProducts(Model model){
        // The attribute name "products" must match th:each in the HTML
        model.addAttribute("products", productRepo.findAll());
        return "productlist"; // 3. Return "productlist" to match the file name
    }

    // 4. Add this new method to serve userlist.html
    @GetMapping("/users-ui")
    public String displayUsers(Model model){
        // The attribute name "users" must match th:each in the new HTML
        model.addAttribute("users", userRepo.findAll());
        return "userlist"; // Return "userlist" to match the file name
    }
}