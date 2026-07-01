package io.coco318213.stubox.user.controller;

import io.coco318213.stubox.common.dto.ApiResponse;
import io.coco318213.stubox.user.dto.SignupRequest;
import io.coco318213.stubox.user.dto.UserCreateResponse;
import io.coco318213.stubox.user.dto.UserResponse;
import io.coco318213.stubox.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ApiResponse<UserCreateResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        UserCreateResponse response = userService.signup(request);

        return ApiResponse.created(response, "가입되었습니다.");
    }

    // 내 계정 정보 조회
    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.ok(userService.getMe(userId), null);
    }
}
