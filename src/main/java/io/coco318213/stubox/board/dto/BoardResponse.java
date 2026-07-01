package io.coco318213.stubox.board.dto;

import io.coco318213.stubox.board.domain.Board;

public record BoardResponse(
        Long boardId,
        String name
) {

    public static BoardResponse from(Board board) {
        return new BoardResponse(
                board.getBoardId(),
                board.getName()
        );
    }
}
