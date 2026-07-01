package io.coco318213.stubox.post.domain;

import io.coco318213.stubox.board.domain.Board;
import io.coco318213.stubox.common.BaseEntity;
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
        name = "post",
        indexes = {
                @Index(name = "idx_post_user_id", columnList = "user_id"),
                @Index(name = "idx_post_board_id", columnList = "board_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 50)
    private String nickname;

    private Post(User user, Board board, String title, String content) {
        this.user = user;
        this.board = board;
        this.title = title;
        this.content = content;
        this.nickname = user.getNickname();
    }

    public static Post create(User user, Board board, String title, String content) {
        return new Post(user, board, title, content);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public boolean isWrittenBy(Long userId) {
        return user.getUserId().equals(userId);
    }
}
