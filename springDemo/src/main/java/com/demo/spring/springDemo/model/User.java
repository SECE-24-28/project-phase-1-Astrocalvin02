package com.demo.spring.springDemo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    private int id;
    private String name;
    private String email;

    public User(){}
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
