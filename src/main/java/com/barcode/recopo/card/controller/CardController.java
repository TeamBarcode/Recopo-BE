package com.barcode.recopo.card.controller;

import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.card.dto.CardRequestDto;
import com.barcode.recopo.card.dto.CardResponseDto;
import com.barcode.recopo.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(
            @AuthenticationPrincipal Long memberId,
            @RequestBody CardRequestDto requestDto
    ) {
        CardResponseDto response = cardService.createCard(memberId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CardResponseDto>> getAllCards(
            @AuthenticationPrincipal Long memberId,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false, defaultValue = "latest") String sort
    ) {
        List<CardResponseDto> cards = cardService.getAllCards(memberId, category, sort);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> getCardById(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cardId
    ) {
        // memberId를 서비스에 전달하여 "나의 카드"인지 검증하게 함
        return ResponseEntity.ok(cardService.getCardById(memberId, cardId));
    }
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cardId
    ) {
        cardService.deleteCard(cardId, memberId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cardId,
            @RequestBody CardRequestDto requestDto
    ) {
        CardResponseDto response = cardService.updateCard(memberId, cardId, requestDto);
        return ResponseEntity.ok(response);
    }
}