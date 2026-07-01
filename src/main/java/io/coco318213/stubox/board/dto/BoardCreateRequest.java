package io.coco318213.stubox.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardCreateRequest(
        @NotBlank(message = "게시판 이름은 필수입니다.")
        @Size(max = 50, message = "게시판 이름은 50자 이하여야 합니다.")
        String name
) {
}
