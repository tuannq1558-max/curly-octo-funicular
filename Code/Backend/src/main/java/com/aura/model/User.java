package com.aura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;



@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ham tu dong tao id cho tung user
    private Long id;
    @Column(unique = true, nullable = false) // email la doc nhat, ko duoc trung, cung khong duoc bo trong
    private String email;
    @Column(nullable = false) // hash password
    private String passwordHash;
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
}