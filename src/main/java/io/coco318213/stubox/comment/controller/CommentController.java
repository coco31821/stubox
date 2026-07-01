package io.coco318213.stubox.comment.controller;

import io.coco318213.stubox.common.dto.ApiResponse;
import io.coco318213.stubox.comment.dto.CommentCreateRequest;
import io.coco318213.stubox.comment.dto.CommentResponse;
import io.coco318213.stubox.comment.dto.CommentUpdateRequest;
import io.coco318213.stubox.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/api/v1/posts/{postId}/comments")
    public ApiResponse<CommentResponse> create(
            Authentication authentication,
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.created(commentService.create(userId, postId, request), "댓글이 작성되었습니다.");
    }

    // 게시글별 댓글 목록 조회
    @GetMapping("/api/v1/posts/{postId}/comments")
    public ApiResponse<List<CommentResponse>> getComments(
            @PathVariable Long postId
    ) {
        return ApiResponse.ok(commentService.getComments(postId), null);
    }

    // 댓글 수정
    @PatchMapping("/api/v1/comments/{commentId}")
    public ApiResponse<CommentResponse> update(
            Authentication authentication,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.ok(commentService.update(userId, commentId, request), "댓글이 수정되었습니다.");
    }

    // 댓글 삭제
    @DeleteMapping("/api/v1/comments/{commentId}")
    public ApiResponse<Void> delete(
            Authentication authentication,
            @PathVariable Long commentId
    ) {
        Long userId = (Long) authentication.getPrincipal();
        commentService.delete(userId, commentId);

        return ApiResponse.ok(null, "댓글이 삭제되었습니다.");
    }
}
