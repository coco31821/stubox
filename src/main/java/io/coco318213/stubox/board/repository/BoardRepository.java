package io.coco318213.stubox.board.repository;

import io.coco318213.stubox.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    boolean existsByName(String name);
}
