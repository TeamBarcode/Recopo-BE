package com.barcode.recopo.idea.service;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.repository.CardRepository;
import com.barcode.recopo.global.exception.CustomException;
import com.barcode.recopo.global.exception.ErrorCode;
import com.barcode.recopo.idea.domain.Idea;
import com.barcode.recopo.idea.dto.IdeaResponseDto;
import com.barcode.recopo.idea.repository.IdeaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final CardRepository cardRepository;

    @Transactional
    public void saveAsIdea(Long cardId, Long memberId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));

        if (!card.getMember().getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CARD_ACCESS);
        }
        if (card.isConverted()) {
            throw new CustomException(ErrorCode.ALREADY_CONVERTED_CARD);
        }
        Idea idea = Idea.create(card);
        ideaRepository.save(idea);

        card.convertToIdea();
    }
    public List<IdeaResponseDto> findAllIdeas(Long memberId) {
        return ideaRepository.findAllByMember_MemberId(memberId).stream()
                .map(IdeaResponseDto::from) // Entity -> DTO 변환
                .toList();
    }

    // 2. 상세 조회
    public IdeaResponseDto findOneIdea(Long ideaId, Long memberId) {
        Idea idea = ideaRepository.findByIdeaIdAndMember_MemberId(ideaId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_IDEA_ACCESS));
        return IdeaResponseDto.from(idea);
    }
}