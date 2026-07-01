package io.coco318213.stubox.comment.service;

import io.coco318213.stubox.comment.domain.Comment;
import io.coco318213.stubox.comment.dto.CommentCreateRequest;
import io.coco318213.stubox.comment.dto.CommentResponse;
import io.coco318213.stubox.comment.dto.CommentUpdateRequest;
import io.coco318213.stubox.comment.exception.CommentNotFoundException;
import io.coco318213.stubox.comment.repository.CommentRepository;
import io.coco318213.stubox.post.domain.Post;
import io.coco318213.stubox.post.exception.PostAccessDeniedException;
import io.coco318213.stubox.post.service.PostService;
import io.coco318213.stubox.user.domain.User;
import io.coco318213.stubox.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    // 댓글 작성
    @Transactional
    public CommentResponse create(Long userId, Long postId, CommentCreateRequest request) {
        User user = userService.requireExists(userId);
        Post post = postService.requireExists(postId);
        Comment comment = commentRepository.save(
                Comment.create(post, user, request.content())
        );

        return CommentResponse.from(comment);
    }

    // 게시글별 댓글 목록 조회
    public List<CommentResponse> getComments(Long postId) {
        postService.requireExists(postId);

        return commentRepository.findByPost_PostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    // 댓글 수정
    @Transactional
    public CommentResponse update(Long userId, Long commentId, CommentUpdateRequest request) {
        Comment comment = requireExists(commentId);
        validateWriter(comment, userId);

        comment.update(request.content());

        return CommentResponse.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long userId, Long commentId) {
        Comment comment = requireExists(commentId);
        validateWriter(comment, userId);

        commentRepository.delete(comment);
    }

    // 댓글 존재 확인
    private Comment requireExists(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    // 댓글 작성자 확인
    private void validateWriter(Comment comment, Long userId) {
        if (!comment.isWrittenBy(userId)) {
            throw new PostAccessDeniedException();
        }
    }
}
