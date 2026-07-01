package io.coco318213.stubox.user.service;


import io.coco318213.stubox.common.config.security.TokenProvider;
import io.coco318213.stubox.user.domain.RefreshToken;
import io.coco318213.stubox.user.domain.User;
import io.coco318213.stubox.user.dto.*;
import io.coco318213.stubox.user.exception.DuplicateEmailException;
import io.coco318213.stubox.user.exception.DuplicateNicknameException;
import io.coco318213.stubox.user.exception.InvalidRefreshTokenException;
import io.coco318213.stubox.user.exception.LoginFailedException;
import io.coco318213.stubox.user.exception.UserNotFoundException;
import io.coco318213.stubox.user.repository.RefreshTokenRepository;
import io.coco318213.stubox.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    // 회원가입
    @Transactional
    public UserCreateResponse signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }
        if (userRepository.existsByNickname(request.nickname())) {
            throw new DuplicateNicknameException();
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .nickname(request.nickname())
                .build();

        User savedUser = userRepository.save(user);

        return UserCreateResponse.from(savedUser);
    }

    // 내 계정 정보 조회
    public UserResponse getMe(Long userId) {
        User user = requireExists(userId);

        return UserResponse.from(user);
    }


    // 유저 존재 확인
    public User requireExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    // 로그인
    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new LoginFailedException();
        }

        user.updateLastLoginAt();

        String accessToken = tokenProvider.issueAccessToken(
                user.getUserId(),
                user.getRole()
        );

        String refreshToken = UUID.randomUUID().toString();

        LocalDateTime refreshTokenExpiredAt = LocalDateTime.now()
                .plusSeconds(tokenProvider.getRefreshTokenExpirationSeconds());

        refreshTokenRepository.save(
                RefreshToken.create(user, refreshToken, refreshTokenExpiredAt)
        );

        return new TokenResponse(
                accessToken,
                refreshToken,
                tokenProvider.getAccessTokenExpirationSeconds()
        );
    }

    // 리프레시 토큰 재발급
    @Transactional
    public TokenResponse refresh(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(InvalidRefreshTokenException::new);

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidRefreshTokenException();
        }

        User user = refreshToken.getUser();

        String newAccessToken = tokenProvider.issueAccessToken(
                user.getUserId(),
                user.getRole()
        );

        String newRefreshToken = UUID.randomUUID().toString();

        LocalDateTime newRefreshTokenExpiredAt = LocalDateTime.now()
                .plusSeconds(tokenProvider.getRefreshTokenExpirationSeconds());

        refreshToken.rotate(newRefreshToken, newRefreshTokenExpiredAt);

        return new TokenResponse(
                newAccessToken,
                newRefreshToken,
                tokenProvider.getAccessTokenExpirationSeconds()
        );
    }

    // 로그아웃
    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteAllByUser_UserId(userId);
    }



}
