package com.barcode.recopo.card.service;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.dto.CardRequestDto;
import com.barcode.recopo.card.dto.CardResponseDto;
import com.barcode.recopo.card.repository.CardRepository;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CardResponseDto createCard(Long memberId, CardRequestDto requestDto) {
        String extractedHashtags = extractHashtags(requestDto.content());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Card card = Card.create(
                requestDto.title(),
                requestDto.content(),
                requestDto.category(),
                extractedHashtags,
                member
        );

        Card savedCard = cardRepository.save(card);

        return new CardResponseDto(
                savedCard.getCardId(),
                savedCard.getMember().getMemberId(),
                savedCard.getTitle(),
                savedCard.getContent(),
                savedCard.getHashtag(),
                savedCard.getCategory(),
                savedCard.getCreatedAt(),
                savedCard.getUpdatedAt()
        );
    }

    public List<CardResponseDto> getAllCards(Long memberId) {
        return cardRepository.findAll().stream()
                .map(card -> new CardResponseDto(
                        card.getCardId(),
                        card.getMember().getMemberId(),
                        card.getTitle(),
                        card.getContent(),
                        card.getHashtag(),
                        card.getCategory(),
                        card.getCreatedAt(),
                        card.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    public CardResponseDto getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다. ID: " + cardId));

        return new CardResponseDto(
                card.getCardId(),
                card.getMember().getMemberId(),
                card.getTitle(),
                card.getContent(),
                card.getHashtag(),
                card.getCategory(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }

    private String extractHashtags(String content) {
        if (content == null || content.isEmpty()) return "";

        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(content);

        return matcher.results()
                .map(mr -> mr.group(1))
                .distinct()
                .collect(Collectors.joining(","));
    }
}