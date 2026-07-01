package io.coco318213.stubox.board.service;

import io.coco318213.stubox.board.domain.Board;
import io.coco318213.stubox.board.dto.BoardCreateRequest;
import io.coco318213.stubox.board.dto.BoardResponse;
import io.coco318213.stubox.board.exception.BoardNotFoundException;
import io.coco318213.stubox.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시판 생성
    @Transactional
    public BoardResponse create(BoardCreateRequest request) {
        Board board = boardRepository.save(Board.create(request.name()));

        return BoardResponse.from(board);
    }

    // 게시판 목록 조회
    public List<BoardResponse> getBoards() {
        return boardRepository.findAll()
                .stream()
                .map(BoardResponse::from)
                .toList();
    }

    // 게시판 존재 확인
    public Board requireExists(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }
}
