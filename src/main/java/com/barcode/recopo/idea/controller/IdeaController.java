package com.barcode.recopo.idea.controller;

import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.idea.domain.IdeaSortBy;
import com.barcode.recopo.idea.domain.Visibility;
import com.barcode.recopo.idea.dto.IdeaRequestDto;
import com.barcode.recopo.idea.dto.IdeaResponseDto;
import com.barcode.recopo.idea.service.IdeaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ideas")
@RequiredArgsConstructor
public class IdeaController {

    private final IdeaService ideaService;

    @PostMapping("/cards/{cardId}/ideas")
    public ResponseEntity<Void> saveIdea(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cardId,
            @RequestBody IdeaRequestDto.Save request
    ) {
        ideaService.saveAsIdea(cardId, memberId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<IdeaResponseDto>> getAllIdeas(
            @AuthenticationPrincipal Long memberId,
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "sortBy", required = false, defaultValue = "LATEST") IdeaSortBy sortBy,
            @RequestParam(value = "visibility", required = false) Visibility visibility
    ) {
        return ResponseEntity.ok(ideaService.findAllIdeas(memberId, category, keyword, sortBy, visibility));
    }

    @GetMapping("/{ideaId}")
    public ResponseEntity<IdeaResponseDto> getOneIdea(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long ideaId
    ) {
        return ResponseEntity.ok(ideaService.findOneIdea(ideaId, memberId));
    }
    @PutMapping("/{ideaId}")
    public ResponseEntity<IdeaResponseDto> updateIdea(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long ideaId,
            @RequestBody IdeaRequestDto.Update requestDto
    ) {
        IdeaResponseDto response = ideaService.updateIdea(memberId, ideaId, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{ideaId}")
    public ResponseEntity<Void> deleteIdea(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long ideaId
    ) {
        ideaService.deleteIdea(ideaId, memberId);
        return ResponseEntity.ok().build();
    }


}