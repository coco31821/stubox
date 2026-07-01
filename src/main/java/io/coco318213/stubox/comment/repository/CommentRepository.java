package io.coco318213.stubox.comment.repository;

import io.coco318213.stubox.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost_PostIdOrderByCreatedAtAsc(Long postId);
}
