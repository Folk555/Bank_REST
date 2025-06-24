package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        return toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Юзер не найден"));
        return toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
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
