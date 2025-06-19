package com.example.bankcards.dto;

import lombok.Data;

import java.util.List;

@Data
public class BankUserDto {
    private Long id;
    private String name;
    private List<CardDto> cards;

}
