package io.coco318213.stubox.user.dto;


import io.coco318213.stubox.user.constants.Role;

public record TokenBody(
        Long userId,
        Role role
) {
}
