package io.coco318213.stubox.comment.dto;

import io.coco318213.stubox.comment.domain.Comment;

import java.time.format.DateTimeFormatter;

public record CommentResponse(
        Long commentId,
        Long postId,
        Long userId,
        String nickname,
        String content,
        String createdAt,
        String updatedAt
) {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getPost().getPostId(),
                comment.getUser().getUserId(),
                comment.getNickname(),
                comment.getContent(),
                comment.getCreatedAt() == null ? null : comment.getCreatedAt().format(FORMATTER),
                comment.getUpdatedAt() == null ? null : comment.getUpdatedAt().format(FORMATTER)
        );
    }
}
