package com.example.bankcards.controller;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BankUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;

    @BeforeEach
    void setup() throws Exception {
        userRepository.deleteAll();

        User user = new User();
        user.setName("BeforeEach");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("fff"));
        user.setRole(Role.ADMIN);
        user.setEnabled(true);
        userRepository.save(user);

        String loginPayload = """
            {
              "username": "admin",
              "password": "fff"
            }
            """;

        String response = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        token = response;
    }

    @Test
    void getUsers_returnAllUsers() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("BeforeEach"));
    }
}
