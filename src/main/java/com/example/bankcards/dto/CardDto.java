package com.example.bankcards.dto;

import com.example.bankcards.entity.BankUser;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CardDto {

    private Long id;
    private String maskedCardNum;
    private LocalDate expirationDate;
    private String status;
    private BigDecimal balance;
    private Long userId;

    public static String maskCardNumber(String number) {
        return "**** **** **** " + number.substring(number.length() - 4);
    }

    public static CardDto getFromCardEntity(Card card) {
        CardDto dto = new CardDto();
        dto.setId(card.getId());
        dto.setMaskedCardNum(CardDto.maskCardNumber(card.getCardNum()));
        dto.setExpirationDate(card.getExpirationDate());
        dto.setStatus(card.getStatus().name());
        dto.setBalance(card.getBalance());
        dto.setUserId(card.getUser().getId());
        return dto;
    }

    public static Card transformToEntity(CardDto dto, BankUser user) {
        Card card = new Card();
        card.setCardNum(dto.getMaskedCardNum());
        card.setExpirationDate(dto.getExpirationDate());
        card.setStatus(CardStatus.valueOf(dto.getStatus()));
        card.setBalance(dto.getBalance());
        card.setUser(user);
        return card;
    }
}
