package com.example.bankcards.security;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}