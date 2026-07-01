package io.coco318213.stubox.post.dto;

import io.coco318213.stubox.post.domain.Post;

import java.time.format.DateTimeFormatter;

public record PostResponse(
        Long postId,
        Long boardId,
        String boardName,
        Long userId,
        String nickname,
        String title,
        String content,
        String createdAt,
        String updatedAt
) {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getPostId(),
                post.getBoard().getBoardId(),
                post.getBoard().getName(),
                post.getUser().getUserId(),
                post.getNickname(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt() == null ? null : post.getCreatedAt().format(FORMATTER),
                post.getUpdatedAt() == null ? null : post.getUpdatedAt().format(FORMATTER)
        );
    }
}
