package com.barcode.recopo.card.service;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.dto.CardRequestDto;
import com.barcode.recopo.card.dto.CardResponseDto;
import com.barcode.recopo.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;

    @Transactional
    public CardResponseDto createCard(CardRequestDto requestDto) {
        Card card = Card.builder()
                .title(requestDto.title())
                .content(requestDto.content())
                .build();

        Card savedCard = cardRepository.save(card);

        return new CardResponseDto(
                savedCard.getCardId(),
                savedCard.getTitle(),
                savedCard.getContent(),
                savedCard.getCreatedAt(),
                savedCard.getUpdatedAt()
        );
    }
    public List<CardResponseDto> getAllCards() {
        return cardRepository.findAll().stream()
                .map(card -> new CardResponseDto(
                        card.getCardId(),
                        card.getTitle(),
                        card.getContent(),
                        card.getCreatedAt(),
                        card.getUpdatedAt()))
                .toList();
    }
    public CardResponseDto getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다. ID: " + cardId));

        return new CardResponseDto(
                card.getCardId(),
                card.getTitle(),
                card.getContent(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }
}