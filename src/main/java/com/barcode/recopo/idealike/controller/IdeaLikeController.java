package com.barcode.recopo.idealike.controller;

import com.barcode.recopo.idea.dto.IdeaResponseDto;
import com.barcode.recopo.idealike.dto.response.IdeaLikeResponse;
import com.barcode.recopo.idealike.service.IdeaLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ideas")
public class IdeaLikeController {
    private final IdeaLikeService ideaLikeService;

    @PostMapping("/{ideaId}/likes")
    public ResponseEntity<IdeaLikeResponse> likeIdea(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long ideaId
    ){
        IdeaLikeResponse response=ideaLikeService.likeIdea(memberId, ideaId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{ideaId}/likes")
    public ResponseEntity<IdeaLikeResponse> unlikeIdea(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long ideaId
    ) {
        IdeaLikeResponse response=ideaLikeService.unlikeIdea(memberId, ideaId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{ideaId}/likes")
    public ResponseEntity<IdeaLikeResponse> getIdeaLikeStatus(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long ideaId
    ){
        IdeaLikeResponse response=ideaLikeService.getIdeaLikeStatus(memberId, ideaId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/liked")
    public ResponseEntity<List<IdeaResponseDto>> getLikedIdeas(
            @AuthenticationPrincipal Long memberId
    ){
        return ResponseEntity.ok(
                ideaLikeService.getLikedIdeas(memberId)
        );
    }

}
