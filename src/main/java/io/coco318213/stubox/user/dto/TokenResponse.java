package io.coco318213.stubox.user.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        Long accessExpiresInSeconds
)
{

}
