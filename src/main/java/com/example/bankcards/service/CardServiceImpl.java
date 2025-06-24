package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Override
    public CardDto create(CardDto dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("юзер не найден"));

        Card card = CardDto.transformToEntity(dto, user);
        return CardDto.getFromCardEntity(cardRepository.save(card));
    }

    @Override
    @Transactional(readOnly = true)
    public CardDto getById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("карта не найдена"));
        return CardDto.getFromCardEntity(card);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getAllPaged(int page, int size) {
        return cardRepository.findAll(PageRequest.of(page, size))
                .map(CardDto::getFromCardEntity);
    }

    @Override
    public CardDto update(Long id, CardDto dto) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("карта не найдена"));

        card.setExpirationDate(dto.getExpirationDate());
        card.setStatus(CardStatus.valueOf(dto.getStatus()));
        card.setBalance(dto.getBalance());
        return CardDto.getFromCardEntity(cardRepository.save(card));
    }

    @Override
    public void delete(Long id) {
        cardRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getUserCards(String username, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return cardRepository.findCardsByUserId(user.getId(), PageRequest.of(page, size))
                .map(CardDto::getFromCardEntity);
    }

    @Override
    public void blockCard(Long cardId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Карта не найдена"));

        if (!card.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Нет доступа к карте");
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Override
    public void setCardStatus(Long id, CardStatus status) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Карта не найдена"));

        card.setStatus(status);
        cardRepository.save(card);
    }
}
