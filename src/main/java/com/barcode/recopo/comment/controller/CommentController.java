package com.barcode.recopo.comment.controller;

import com.barcode.recopo.comment.dto.request.CommentCreateRequest;
import com.barcode.recopo.comment.dto.request.CommentUpdateRequest;
import com.barcode.recopo.comment.dto.response.CommentResponse;
import com.barcode.recopo.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    //댓글 등록
    @PostMapping("/ideas/{ideaId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long ideaId,
            @Valid @RequestBody CommentCreateRequest request
    ){
        CommentResponse response=commentService.createComment(memberId, ideaId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //아이디어의 댓글 목록 조회
    @GetMapping("/ideas/{ideaId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long ideaId
    ){
        return ResponseEntity.ok(
                commentService.getComments(ideaId)
        );
    }

    //댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request
    ){
        return ResponseEntity.ok(
                commentService.updateComment(
                        memberId,
                        commentId,
                        request
                )
        );
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long commentId
    ){
        commentService.deleteComment(memberId, commentId);

        return ResponseEntity.noContent().build();
    }
}
