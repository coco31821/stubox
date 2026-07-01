package io.coco318213.stubox.web.controller;

import io.coco318213.stubox.board.service.BoardService;
import io.coco318213.stubox.comment.service.CommentService;
import io.coco318213.stubox.post.dto.PostResponse;
import io.coco318213.stubox.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final BoardService boardService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/")
    public String home() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String posts(
            @RequestParam(required = false) Long boardId,
            Model model
    ) {
        model.addAttribute("boards", boardService.getBoards());
        model.addAttribute("selectedBoardId", boardId);
        model.addAttribute("posts", boardId == null
                ? postService.getPosts()
                : postService.getPostsByBoard(boardId));

        return "posts/list";
    }

    @GetMapping("/posts/{postId}")
    public String postDetail(
            @PathVariable Long postId,
            Model model
    ) {
        model.addAttribute("post", postService.getPost(postId));
        model.addAttribute("comments", commentService.getComments(postId));

        return "posts/detail";
    }

    @GetMapping("/posts/new")
    public String newPost(Model model) {
        model.addAttribute("boards", boardService.getBoards());
        model.addAttribute("isEdit", false);

        return "posts/form";
    }

    @GetMapping("/posts/{postId}/edit")
    public String editPost(
            @PathVariable Long postId,
            Model model
    ) {
        PostResponse post = postService.getPost(postId);

        model.addAttribute("post", post);
        model.addAttribute("boards", boardService.getBoards());
        model.addAttribute("isEdit", true);

        return "posts/form";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @GetMapping("/me")
    public String me() {
        return "users/me";
    }
}
