package com.example.bankcards.service;

import com.example.bankcards.dto.BankUserDto;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.BankUser;
import com.example.bankcards.repository.BankUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BankUserServiceImpl implements BankUserService {

    private final BankUserRepository userRepository;

    @Override
    public BankUserDto create(BankUserDto dto) {
        BankUser user = new BankUser();
        user.setName(dto.getName());
        return toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public BankUserDto getById(Long id) {
        BankUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Юзер не найден"));
        return toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankUserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private BankUserDto toDto(BankUser user) {
        BankUserDto dto = new BankUserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setCards(
                user.getCards().stream()
                        .map(CardDto::getFromCardEntity)
                        .toList()
        );
        return dto;
    }
}
