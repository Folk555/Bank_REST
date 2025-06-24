package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto dto);
    UserDto getById(Long id);
    List<UserDto> getAll();
    void delete(Long id);
}
