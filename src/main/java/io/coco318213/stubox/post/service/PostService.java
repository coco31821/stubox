package io.coco318213.stubox.post.service;

import io.coco318213.stubox.board.domain.Board;
import io.coco318213.stubox.board.service.BoardService;
import io.coco318213.stubox.post.domain.Post;
import io.coco318213.stubox.post.dto.PostCreateRequest;
import io.coco318213.stubox.post.dto.PostResponse;
import io.coco318213.stubox.post.dto.PostUpdateRequest;
import io.coco318213.stubox.post.exception.PostAccessDeniedException;
import io.coco318213.stubox.post.exception.PostNotFoundException;
import io.coco318213.stubox.post.repository.PostRepository;
import io.coco318213.stubox.user.domain.User;
import io.coco318213.stubox.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardService boardService;
    private final UserService userService;

    // 게시글 작성
    @Transactional
    public PostResponse create(Long userId, PostCreateRequest request) {
        User user = userService.requireExists(userId);
        Board board = boardService.requireExists(request.boardId());
        Post post = postRepository.save(
                Post.create(user, board, request.title(), request.content())
        );

        return PostResponse.from(post);
    }

    // 게시글 목록 조회
    public List<PostResponse> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    // 게시판별 게시글 목록 조회
    public List<PostResponse> getPostsByBoard(Long boardId) {
        boardService.requireExists(boardId);

        return postRepository.findByBoard_BoardIdOrderByCreatedAtDesc(boardId)
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    // 게시글 상세 조회
    public PostResponse getPost(Long postId) {
        return PostResponse.from(requireExists(postId));
    }

    // 게시글 수정
    @Transactional
    public PostResponse update(Long userId, Long postId, PostUpdateRequest request) {
        Post post = requireExists(postId);
        validateWriter(post, userId);

        post.update(request.title(), request.content());

        return PostResponse.from(post);
    }

    // 게시글 삭제
    @Transactional
    public void delete(Long userId, Long postId) {
        Post post = requireExists(postId);
        validateWriter(post, userId);

        postRepository.delete(post);
    }

    // 게시글 존재 확인
    public Post requireExists(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    // 게시글 작성자 확인
    private void validateWriter(Post post, Long userId) {
        if (!post.isWrittenBy(userId)) {
            throw new PostAccessDeniedException();
        }
    }
}
