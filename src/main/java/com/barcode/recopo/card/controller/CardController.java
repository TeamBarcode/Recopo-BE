package com.barcode.recopo.card.controller;

import com.barcode.recopo.card.dto.CardRequestDto;
import com.barcode.recopo.card.dto.CardResponseDto;
import com.barcode.recopo.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    @PostMapping
    public CardResponseDto createCard(@RequestBody CardRequestDto requestDto) {
        return cardService.createCard(requestDto);
    }
    @GetMapping
    public List<CardResponseDto> getAllCards() {
        return cardService.getAllCards();
    }
    @GetMapping("/{cardId}")
    public CardResponseDto getCardById(@PathVariable Long cardId) {
        return cardService.getCardById(cardId);
    }
}