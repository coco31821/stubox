package io.coco318213.stubox.user.controller;


import io.coco318213.stubox.common.dto.ApiResponse;
import io.coco318213.stubox.user.dto.LoginRequest;
import io.coco318213.stubox.user.dto.RefreshRequest;
import io.coco318213.stubox.user.dto.TokenResponse;
import io.coco318213.stubox.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 로그인
    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(
            @Valid @RequestBody LoginRequest request
    ){
        TokenResponse response = userService.login(request);

        return ApiResponse.ok(response,"로그인되었습니다.");
    }

    // 리프레시 토큰 재발급
    @PostMapping("/refresh")
    public ApiResponse<TokenResponse> refreshToken(
            @Valid @RequestBody RefreshRequest request
    ){
        TokenResponse response = userService.refresh(request);

        return ApiResponse.ok(response,null);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<Void> logout(Authentication authentication){
        Long userId = (Long) authentication.getPrincipal();

        userService.logout(userId);

        return ApiResponse.ok(null,"로그아웃되었습니다.");
    }



}
