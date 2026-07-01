package io.coco318213.stubox.comment.domain;

import io.coco318213.stubox.common.BaseEntity;
import io.coco318213.stubox.post.domain.Post;
import io.coco318213.stubox.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "comment",
        indexes = {
                @Index(name = "idx_comment_post_id", columnList = "post_id"),
                @Index(name = "idx_comment_user_id", columnList = "user_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Lob
    @Column(nullable = false)
    private String content;

    private Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.nickname = user.getNickname();
        this.content = content;
    }

    public static Comment create(Post post, User user, String content) {
        return new Comment(post, user, content);
    }

    public void update(String content) {
        this.content = content;
    }

    public boolean isWrittenBy(Long userId) {
        return user.getUserId().equals(userId);
    }
}
