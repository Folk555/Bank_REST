package com.example.bankcards.service;

import com.example.bankcards.dto.BankUserDto;

import java.util.List;

public interface BankUserService {
    BankUserDto create(BankUserDto dto);
    BankUserDto getById(Long id);
    List<BankUserDto> getAll();
    void delete(Long id);
}
