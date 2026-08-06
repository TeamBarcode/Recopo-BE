package com.barcode.recopo.card.service;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.domain.CardSortBy;
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        validateCardRequest(requestDto);
        Card card = Card.create(
                requestDto.title(),
                requestDto.content(),
                requestDto.category(),
                requestDto.hashtag(),
                member
        );

        Card savedCard = cardRepository.save(card);

        return convertToDto(savedCard);
    }

    public List<CardResponseDto> getAllCards(Long memberId, Category category, String keyword, CardSortBy sortBy) {
        CardSortBy sortCriteria = (sortBy != null) ? sortBy : CardSortBy.LATEST;

        Sort sort;
        switch (sortCriteria) {
            case OLDEST:
                sort = Sort.by(Sort.Direction.ASC, "createdAt");
                break;
            case MODIFIED:
                sort = Sort.by(Sort.Direction.DESC, "updatedAt");
                break;
            case LATEST:
            default:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
        }

        List<Card> cards;
        if (category == null) {
            if (keyword == null || keyword.isBlank()) {
                cards = cardRepository.findByMemberMemberIdAndIsConvertedFalse(memberId, sort);
            } else {
                cards = cardRepository.findAllByMember_MemberIdAndHashtagContaining(memberId, keyword, sort);
            }
        } else {
            if (keyword == null || keyword.isBlank()) {
                cards = cardRepository.findByMemberMemberIdAndCategoryAndIsConvertedFalse(memberId, category, sort);
            } else {
                cards = cardRepository.findAllByMember_MemberIdAndCategoryAndHashtagContaining(memberId, category, keyword, sort);
            }
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


    @Transactional
    public void deleteCard(Long cardId, Long memberId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));

        if (!card.getMember().getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CARD_ACCESS);
        }
        if (card.isConverted()) {
            throw new CustomException(ErrorCode.CANNOT_DELETE_CONVERTED_CARD);
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
        validateCardRequest(requestDto);
        card.update(requestDto.title(), requestDto.content(), requestDto.category(), requestDto.hashtag());
        return convertToDto(card);
    }
    private void validateCardRequest(CardRequestDto requestDto) {
        if (requestDto.title() == null || requestDto.title().isBlank()) {
            throw new CustomException(ErrorCode.TITLE_REQUIRED);
        }
        if (requestDto.content() == null || requestDto.content().isBlank()) {
            throw new CustomException(ErrorCode.CONTENT_REQUIRED);
        }
        if (requestDto.category() == null) {
            throw new CustomException(ErrorCode.CATEGORY_REQUIRED);
        }

        String hashtag = requestDto.hashtag();
        if (hashtag != null && !hashtag.isBlank()) {
            String[] tags = hashtag.split(",");
            if (tags.length > 5) {
                throw new CustomException(ErrorCode.TOO_MANY_HASHTAGS);
            }
        }
    }
}