package io.coco318213.stubox;

import io.coco318213.stubox.board.dto.BoardCreateRequest;
import io.coco318213.stubox.board.dto.BoardResponse;
import io.coco318213.stubox.board.service.BoardService;
import io.coco318213.stubox.comment.dto.CommentCreateRequest;
import io.coco318213.stubox.comment.dto.CommentResponse;
import io.coco318213.stubox.comment.dto.CommentUpdateRequest;
import io.coco318213.stubox.comment.service.CommentService;
import io.coco318213.stubox.post.dto.PostCreateRequest;
import io.coco318213.stubox.post.dto.PostResponse;
import io.coco318213.stubox.post.dto.PostUpdateRequest;
import io.coco318213.stubox.post.exception.PostAccessDeniedException;
import io.coco318213.stubox.post.service.PostService;
import io.coco318213.stubox.user.dto.SignupRequest;
import io.coco318213.stubox.user.dto.UserCreateResponse;
import io.coco318213.stubox.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BlogServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Test
    void 게시글과_댓글을_작성하고_수정하고_삭제한다() {
        UserCreateResponse user = signup("author@example.com", "author");
        BoardResponse board = boardService.create(new BoardCreateRequest("notice"));

        PostResponse post = postService.create(
                user.userId(),
                new PostCreateRequest(board.boardId(), "첫 글", "첫 본문")
        );

        assertNotNull(post.postId());
        assertEquals(board.boardId(), post.boardId());
        assertEquals(user.userId(), post.userId());
        assertEquals("author", post.nickname());
        assertEquals("첫 글", post.title());
        assertEquals("첫 본문", post.content());

        PostResponse updatedPost = postService.update(
                user.userId(),
                post.postId(),
                new PostUpdateRequest("수정 글", "수정 본문")
        );

        assertEquals("수정 글", updatedPost.title());
        assertEquals("수정 본문", updatedPost.content());

        CommentResponse comment = commentService.create(
                user.userId(),
                post.postId(),
                new CommentCreateRequest("첫 댓글")
        );

        assertNotNull(comment.commentId());
        assertEquals(post.postId(), comment.postId());
        assertEquals(user.userId(), comment.userId());
        assertEquals("author", comment.nickname());
        assertEquals("첫 댓글", comment.content());

        CommentResponse updatedComment = commentService.update(
                user.userId(),
                comment.commentId(),
                new CommentUpdateRequest("수정 댓글")
        );

        assertEquals("수정 댓글", updatedComment.content());

        List<PostResponse> posts = postService.getPostsByBoard(board.boardId());
        List<CommentResponse> comments = commentService.getComments(post.postId());

        assertEquals(1, posts.size());
        assertEquals(1, comments.size());

        commentService.delete(user.userId(), comment.commentId());
        assertEquals(0, commentService.getComments(post.postId()).size());

        postService.delete(user.userId(), post.postId());
        assertFalse(postService.getPosts().stream()
                .anyMatch(response -> response.postId().equals(post.postId())));
    }

    @Test
    void 작성자가_아니면_게시글과_댓글을_수정할_수_없다() {
        UserCreateResponse author = signup("author2@example.com", "author2");
        UserCreateResponse other = signup("other@example.com", "other");
        BoardResponse board = boardService.create(new BoardCreateRequest("free"));
        PostResponse post = postService.create(
                author.userId(),
                new PostCreateRequest(board.boardId(), "작성자 글", "작성자 본문")
        );
        CommentResponse comment = commentService.create(
                author.userId(),
                post.postId(),
                new CommentCreateRequest("작성자 댓글")
        );

        assertThrows(
                PostAccessDeniedException.class,
                () -> postService.update(
                        other.userId(),
                        post.postId(),
                        new PostUpdateRequest("다른 사람 수정", "다른 사람 본문")
                )
        );
        assertThrows(
                PostAccessDeniedException.class,
                () -> commentService.update(
                        other.userId(),
                        comment.commentId(),
                        new CommentUpdateRequest("다른 사람 댓글")
                )
        );
    }

    private UserCreateResponse signup(String email, String nickname) {
        return userService.signup(new SignupRequest(email, "password123", nickname));
    }
}
