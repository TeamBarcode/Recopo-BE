package com.barcode.recopo.card.service;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.card.dto.CardRequestDto;
import com.barcode.recopo.card.dto.CardResponseDto;
import com.barcode.recopo.card.repository.CardRepository;
import com.barcode.recopo.global.exception.CustomException;
import com.barcode.recopo.global.exception.ErrorCode;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    private CardResponseDto convertToDto(Card card) {
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

        return convertToDto(savedCard);
    }

    public List<CardResponseDto> getAllCards(Long memberId, Category category, String sortBy) {
        Sort sort;
        if ("oldest".equals(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "createdAt");
        } else if ("updated".equals(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        List<Card> cards;
        if (category == null) {
            // 카테고리가 없으면 전체 조회
            cards = cardRepository.findByMemberMemberId(memberId, sort);
        } else {
            // 카테고리가 있으면 해당 카테고리만 조회
            cards = cardRepository.findByMemberMemberIdAndCategory(memberId, category, sort);
        }
        return cards.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CardResponseDto getCardById(Long memberId, Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));

        if (!card.getMember().getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CARD_ACCESS);
        }
        return convertToDto(card);
    }

    private String extractHashtags(String content) {
        if (content == null || content.isEmpty()) return "";

        Pattern pattern = Pattern.compile("#([\\w가-힣]+)");
        Matcher matcher = pattern.matcher(content);

        return matcher.results()
                .map(mr -> mr.group(1))
                .distinct()
                .collect(Collectors.joining(","));
    }
    @Transactional
    public void deleteCard(Long cardId, Long memberId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));

        if (!card.getMember().getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CARD_ACCESS);
        }
        cardRepository.delete(card);
    }
    @Transactional
    public CardResponseDto updateCard(Long memberId, Long cardId, CardRequestDto requestDto) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));

        if (!card.getMember().getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CARD_ACCESS);
        }
        // 내용이 바뀌면 해시태그도 다시 추출
        String extractedHashtags = extractHashtags(requestDto.content());

        card.update(requestDto.title(), requestDto.content(), requestDto.category(), extractedHashtags);
        return convertToDto(card);
    }
}