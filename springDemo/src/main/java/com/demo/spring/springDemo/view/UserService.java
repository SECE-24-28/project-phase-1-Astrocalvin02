package com.demo.spring.springDemo.view;

import com.demo.spring.springDemo.model.User;
import com.demo.spring.springDemo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.sql.*;

@Service
public class UserService {
    static List<User> users = new ArrayList<>();
    @Autowired
    UserRepo userRepo;

    public boolean addUser(User user){
        userRepo.save(user);
        return true;
    }
    public List<User> getUsers(){
        return userRepo.findAll();
    }
    public User getUser(int id){
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null);
    }
    public User updateUser(int id, User user){
        Optional<User> existingUser = userRepo.findById(id);
        if(existingUser.isPresent()){
            User u = existingUser.get();
            u.setName(user.getName());
            u.setEmail(u.getEmail());
            return userRepo.save(u);
        }
        return null;
    }
    public String deleteUser(int id){
        userRepo.deleteById(id);
        return "User Deleted";
    }
    public List<User> findByName(String name){
        return userRepo.findByName(name);
    }
}