package io.coco318213.stubox.post.controller;

import io.coco318213.stubox.common.dto.ApiResponse;
import io.coco318213.stubox.post.dto.PostCreateRequest;
import io.coco318213.stubox.post.dto.PostResponse;
import io.coco318213.stubox.post.dto.PostUpdateRequest;
import io.coco318213.stubox.post.service.PostService;
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
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping
    public ApiResponse<PostResponse> create(
            Authentication authentication,
            @Valid @RequestBody PostCreateRequest request
    ) {
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.created(postService.create(userId, request), "게시글이 작성되었습니다.");
    }

    // 게시글 목록 조회
    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts() {
        return ApiResponse.ok(postService.getPosts(), null);
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(
            @PathVariable Long postId
    ) {
        return ApiResponse.ok(postService.getPost(postId), null);
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public ApiResponse<PostResponse> update(
            Authentication authentication,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.ok(postService.update(userId, postId, request), "게시글이 수정되었습니다.");
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> delete(
            Authentication authentication,
            @PathVariable Long postId
    ) {
        Long userId = (Long) authentication.getPrincipal();
        postService.delete(userId, postId);

        return ApiResponse.ok(null, "게시글이 삭제되었습니다.");
    }
}
