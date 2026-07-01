package io.coco318213.stubox.user.dto;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
