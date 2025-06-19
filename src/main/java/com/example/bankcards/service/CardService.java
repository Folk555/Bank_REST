package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import org.springframework.data.domain.Page;

public interface CardService {
    CardDto create(CardDto dto);
    CardDto getById(Long id);
    Page<CardDto> getAllPaged(int page, int size);
    CardDto update(Long id, CardDto dto);
    void delete(Long id);
}
