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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MoneyTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    private Long fromCardId;
    private Long toCardId;
    private String token;

    @BeforeEach
    void setup() throws Exception {
        cardRepository.deleteAll();
        userRepository.deleteAll();

        // создаём пользователя с ролью USER
        var user = new User();
        user.setName("BeforeEach");
        user.setUsername("user");
        user.setPassword(new BCryptPasswordEncoder().encode("fff"));
        user.setRole(com.example.bankcards.security.Role.USER);
        user.setEnabled(true);
        user = userRepository.save(user);

        Card cardFrom = new Card(null, "1111222233334444", user, LocalDate.now(), CardStatus.ACTIVE, new BigDecimal("50000.0"));
        cardFrom = cardRepository.save(cardFrom);
        Card cardTo = new Card(null, "1111222233334445", user, LocalDate.now(), CardStatus.ACTIVE, new BigDecimal("20000.0"));
        cardTo = cardRepository.save(cardTo);

        fromCardId = cardFrom.getId();
        toCardId = cardTo.getId();

        String loginPayload = """
        {
          "username": "user",
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
    void transferMoney() throws Exception {
        mockMvc.perform(post("/money/transfer")
                        .header("Authorization", "Bearer " + token)
                        .param("fromCardId", fromCardId.toString())
                        .param("toCardId", toCardId.toString())
                        .param("amount", "5000.0"))
                .andExpect(status().isOk());

        Card cardFrom = cardRepository.findById(fromCardId).orElseThrow();
        Card cardTo = cardRepository.findById(toCardId).orElseThrow();

        assertEquals(new BigDecimal("45000.0"), cardFrom.getBalance());
        assertEquals(new BigDecimal("25000.0"), cardTo.getBalance());
    }
}