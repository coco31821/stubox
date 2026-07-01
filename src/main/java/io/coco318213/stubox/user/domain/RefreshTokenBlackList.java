package io.coco318213.stubox.user.domain;

import io.coco318213.stubox.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenBlackList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenBlackListId;

    @ManyToOne
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;

    public RefreshTokenBlackList(RefreshToken refreshToken) { this.refreshToken = refreshToken; }

}
