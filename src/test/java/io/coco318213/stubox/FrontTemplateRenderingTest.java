package io.coco318213.stubox;

import io.coco318213.stubox.board.dto.BoardCreateRequest;
import io.coco318213.stubox.board.dto.BoardResponse;
import io.coco318213.stubox.board.service.BoardService;
import io.coco318213.stubox.comment.dto.CommentCreateRequest;
import io.coco318213.stubox.comment.service.CommentService;
import io.coco318213.stubox.post.dto.PostCreateRequest;
import io.coco318213.stubox.post.dto.PostResponse;
import io.coco318213.stubox.post.service.PostService;
import io.coco318213.stubox.user.dto.SignupRequest;
import io.coco318213.stubox.user.dto.UserCreateResponse;
import io.coco318213.stubox.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FrontTemplateRenderingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Test
    void 주요_화면이_렌더링된다() throws Exception {
        UserCreateResponse user = userService.signup(new SignupRequest("front@example.com", "password123", "front"));
        BoardResponse board = boardService.create(new BoardCreateRequest("free"));
        PostResponse post = postService.create(
                user.userId(),
                new PostCreateRequest(board.boardId(), "첫 글", "첫 본문")
        );
        commentService.create(user.userId(), post.postId(), new CommentCreateRequest("첫 댓글"));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("첫 글")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("게시판 만들기")));

        mockMvc.perform(get("/posts").param("boardId", board.boardId().toString()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/{postId}", post.postId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("첫 댓글")));

        mockMvc.perform(get("/posts/new"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/{postId}/edit", post.postId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/me"))
                .andExpect(status().isOk());
    }
}
