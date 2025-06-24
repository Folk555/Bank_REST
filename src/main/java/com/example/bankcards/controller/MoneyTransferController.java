package com.example.bankcards.controller;

import com.example.bankcards.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/money")
@RequiredArgsConstructor
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> transferMoney(
            @RequestParam Long fromCardId,
            @RequestParam Long toCardId,
            @RequestParam BigDecimal amount) {
        moneyTransferService.transfer(fromCardId, toCardId, amount);
        return ResponseEntity.ok().build();
    }
}
