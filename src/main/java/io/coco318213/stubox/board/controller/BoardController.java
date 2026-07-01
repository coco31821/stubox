package io.coco318213.stubox.board.controller;

import io.coco318213.stubox.common.dto.ApiResponse;
import io.coco318213.stubox.board.dto.BoardCreateRequest;
import io.coco318213.stubox.board.dto.BoardResponse;
import io.coco318213.stubox.board.service.BoardService;
import io.coco318213.stubox.post.dto.PostResponse;
import io.coco318213.stubox.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final PostService postService;

    // 게시판 생성
    @PostMapping
    public ApiResponse<BoardResponse> create(
            @Valid @RequestBody BoardCreateRequest request
    ) {
        return ApiResponse.created(boardService.create(request), "게시판이 생성되었습니다.");
    }

    // 게시판 목록 조회
    @GetMapping
    public ApiResponse<List<BoardResponse>> getBoards() {
        return ApiResponse.ok(boardService.getBoards(), null);
    }

    // 게시판별 게시글 목록 조회
    @GetMapping("/{boardId}/posts")
    public ApiResponse<List<PostResponse>> getPostsByBoard(
            @PathVariable Long boardId
    ) {
        return ApiResponse.ok(postService.getPostsByBoard(boardId), null);
    }
}
