package com.example.bankcards.controller;

import com.example.bankcards.entity.User;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;

    @BeforeEach
    void setup() throws Exception {
        cardRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("BeforeEach");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("fff"));
        user.setRole(com.example.bankcards.security.Role.ADMIN);
        user.setEnabled(true);
        user = userRepository.save(user);

        Card card = new Card(null, "1234567812345678", user, LocalDate.now(), CardStatus.ACTIVE, BigDecimal.valueOf(100));
        cardRepository.save(card);

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
    void getAllCards_returnCards() throws Exception {
        mockMvc.perform(get("/cards/allCards?page=0&size=10")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].maskedCardNum").value("**** **** **** 5678"));
    }
}
