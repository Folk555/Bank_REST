package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final CardRepository cardRepository;

    @Transactional
    public void transfer(Long fromCardId, Long toCardId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Неверная сумма перевода " + amount);
        }
        if (fromCardId.equals(toCardId)) {
            throw new IllegalArgumentException("Отправитель и получатель не могут совпадать");
        }

        Long firstCardId = fromCardId.compareTo(toCardId) < 0 ? fromCardId : toCardId;
        Long secondCardId = fromCardId.compareTo(toCardId) < 0 ? toCardId : fromCardId;

        Card fromCard = cardRepository.findById(firstCardId)
                .orElseThrow(() -> new RuntimeException("Карта отправителя не найдена"));

        Card toCard = cardRepository.findById(secondCardId)
                .orElseThrow(() -> new RuntimeException("Карта получателя не найдена"));

        if (!fromCard.getUser().getId().equals(toCard.getUser().getId())) {
            throw new RuntimeException("Карты принадлежат разным пользователям");
        }

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на балансе");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        cardRepository.saveAll(List.of(fromCard, toCard));
    }
}
