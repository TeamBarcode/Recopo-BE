package com.barcode.recopo.idea.service;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.card.repository.CardRepository;
import com.barcode.recopo.global.exception.CustomException;
import com.barcode.recopo.global.exception.ErrorCode;
import com.barcode.recopo.idea.domain.Idea;
import com.barcode.recopo.idea.domain.IdeaSortBy;
import com.barcode.recopo.idea.domain.Visibility;
import com.barcode.recopo.idea.dto.IdeaRequestDto;
import com.barcode.recopo.idea.dto.IdeaResponseDto;
import com.barcode.recopo.idea.repository.IdeaRepository;
import com.barcode.recopo.recommendation.domain.Recommendation;
import com.barcode.recopo.recommendation.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final CardRepository cardRepository;
    private final RecommendationRepository recommendationRepository;

    @Transactional
    public void saveAsIdea(Long cardId, Long memberId, IdeaRequestDto.Save requestDto) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));

        if (!card.getMember().getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CARD_ACCESS);
        }
        if (card.isConverted()) {
            throw new CustomException(ErrorCode.ALREADY_CONVERTED_CARD);
        }

        Idea idea;

        // recommendationId가 있는 경우
        if (requestDto.recommendationId() != null) {
            Recommendation recommendation = recommendationRepository.findById(requestDto.recommendationId())
                    .orElseThrow(() -> new CustomException(ErrorCode.RECOMMENDATION_NOT_FOUND));

            idea = Idea.createWithRecommendation(card, requestDto.visibility(), recommendation);
        } else {
            // 추천 결과가 없는 일반 전환인 경우
            idea = Idea.create(card, requestDto.visibility());
        }

        ideaRepository.save(idea);
        card.convertToIdea();
    }

    public List<IdeaResponseDto> findAllIdeas(Long memberId, Category category, String keyword, IdeaSortBy sortBy) {
        IdeaSortBy sortCriteria = (sortBy != null) ? sortBy : IdeaSortBy.LATEST;

        Sort sort;
        switch (sortCriteria) {
            case OLDEST:
                sort = Sort.by(Sort.Direction.ASC, "createdAt");
                break;
            case POPULAR:
                sort = Sort.by(Sort.Direction.DESC, "likeCount");
                break;
            case LATEST:
            default:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
        }
        List<Idea> ideas;
        if (category == null) {
            if (keyword == null || keyword.isBlank()) {
                ideas = ideaRepository.findAllByMember_MemberId(memberId, sort);
            } else {
                ideas = ideaRepository.findAllByMember_MemberIdAndHashtagContaining(memberId, keyword, sort);
            }
        } else {
            if (keyword == null || keyword.isBlank()) {
                ideas = ideaRepository.findAllByMember_MemberIdAndCategory(memberId, category, sort);
            } else {
                ideas = ideaRepository.findAllByMember_MemberIdAndCategoryAndHashtagContaining(memberId, category, keyword, sort);
            }
        }

        return ideas.stream()
                .map(IdeaResponseDto::from)
                .toList();
    }

    public IdeaResponseDto findOneIdea(Long ideaId, Long memberId) {
        Idea idea = ideaRepository.findByIdeaIdAndMember_MemberId(ideaId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_IDEA_ACCESS));
        return IdeaResponseDto.from(idea);
    }
    @Transactional
    public IdeaResponseDto updateIdea(Long memberId, Long ideaId, IdeaRequestDto.Update requestDto) {
        Idea idea = ideaRepository.findByIdeaIdAndMember_MemberId(ideaId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_IDEA_ACCESS));
        validateHashtagCount(requestDto.hashtag());
        idea.update(
                requestDto.title(),
                requestDto.hashtag(),
                requestDto.category(),
                requestDto.visibility()
        );

        return IdeaResponseDto.from(idea);
    }

    @Transactional
    public void deleteIdea(Long ideaId, Long memberId) {
        Idea idea = ideaRepository.findByIdeaIdAndMember_MemberId(ideaId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_IDEA_ACCESS));

        ideaRepository.delete(idea);
    }

    private void validateHashtagCount(String hashtag) {
        if (hashtag == null || hashtag.isBlank()) {
            return;
        }
        String[] tags = hashtag.split(",");
        if (tags.length > 5) {
            throw new CustomException(ErrorCode.TOO_MANY_HASHTAGS);
        }
    }
}