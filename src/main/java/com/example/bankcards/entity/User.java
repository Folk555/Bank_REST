package com.example.bankcards.entity;

import com.example.bankcards.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "bank_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Card> cards;

    @Column(nullable = false, length = 30, unique = true)
    private String username;
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = true;

    @Override
    public String toString() {return "";}
}
