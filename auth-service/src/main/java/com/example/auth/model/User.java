package com.example.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    private String password;

    @Column(nullable = false)
    private String provider = "LOCAL";

    public User(String email, String name, String provider) {
        this.email = email;
        this.name = name;
        this.provider = provider;
    }
}
