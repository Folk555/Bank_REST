package com.example.bankcards.controller;

import com.example.bankcards.entity.BankUser;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.BankUserRepository;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankUserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    void setup() {
        cardRepository.deleteAll();
        userRepository.deleteAll();

        BankUser user = userRepository.save(new BankUser(null, "BeforeEach", List.of()));
        Card card = new Card(null, "1234567812345678", user, LocalDate.now(), CardStatus.ACTIVE, BigDecimal.valueOf(100));
        cardRepository.save(card);
    }

    @Test
    void getAllCards_returnCards() throws Exception {
        mockMvc.perform(get("/cards?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].maskedCardNum").value("**** **** **** 5678"));
    }
}