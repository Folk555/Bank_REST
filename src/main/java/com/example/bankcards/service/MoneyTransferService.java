package com.example.bankcards.service;

import java.math.BigDecimal;

public interface MoneyTransferService {
    void transfer(Long fromCardId, Long toCardId, BigDecimal amount);
}
