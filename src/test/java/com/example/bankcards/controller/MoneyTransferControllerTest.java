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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MoneyTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankUserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    private Long fromCardId;
    private Long toCardId;

    @BeforeEach
    void setup() {
        cardRepository.deleteAll();
        userRepository.deleteAll();

        var user = userRepository.save(new BankUser(null, "BeforeEach", List.of()));
        Card cardFrom = new Card(null, "1111222233334444", user, LocalDate.now(), CardStatus.ACTIVE, new BigDecimal("50000.0"));
        cardFrom = cardRepository.save(cardFrom);
        Card cardTo = new Card(null, "1111222233334445", user, LocalDate.now(), CardStatus.ACTIVE, new BigDecimal("20000.0"));
        cardTo = cardRepository.save(cardTo);

        fromCardId = cardFrom.getId();
        toCardId = cardTo.getId();
    }

    @Test
    void transferMoney() throws Exception {
        mockMvc.perform(post("/money/transfer")
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