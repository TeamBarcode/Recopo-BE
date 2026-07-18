package com.barcode.recopo.idea.controller;

import com.barcode.recopo.idea.dto.IdeaResponseDto;
import com.barcode.recopo.idea.dto.IdeaSaveRequest;
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

    @PostMapping("/save")
    public ResponseEntity<Void> saveIdea(
            @AuthenticationPrincipal Long memberId,
            @RequestBody IdeaSaveRequest request
    ) {
        ideaService.saveAsIdea(request.cardId(), memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<IdeaResponseDto>> getAllIdeas(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(ideaService.findAllIdeas(memberId));
    }

    @GetMapping("/{ideaId}")
    public ResponseEntity<IdeaResponseDto> getOneIdea(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long ideaId
    ) {
        return ResponseEntity.ok(ideaService.findOneIdea(ideaId, memberId));
    }
}