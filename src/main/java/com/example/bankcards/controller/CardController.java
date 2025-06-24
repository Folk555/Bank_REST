package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto dto) {
        return ResponseEntity.ok(cardService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allCards")
    public ResponseEntity<Page<CardDto>> getAllCardsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(cardService.getAllPaged(page, size));
    }

    @PutMapping("/{id}/setCardActivity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> setCardBlocked(
            @PathVariable Long id,
            @RequestParam CardStatus status
    ) {
        cardService.setCardStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }





    @GetMapping("/myCards")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<CardDto>> getUserCardsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal(expression = "username") String username
    ) {
        return ResponseEntity.ok(cardService.getUserCards(username, page, size));
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> blockCard(
            @PathVariable Long id,
            @AuthenticationPrincipal(expression = "username") String username
    ) {
        cardService.blockCard(id, username);
        return ResponseEntity.ok().build();
    }
}

