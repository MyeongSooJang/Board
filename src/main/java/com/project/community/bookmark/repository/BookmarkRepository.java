package com.project.community.bookmark.repository;

import com.project.community.bookmark.entity.Bookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("""
            SELECT bm
            FROM Bookmark bm
            JOIN FETCH bm.member m
            JOIN FETCH bm.board b
            WHERE m.username = :username AND b.boardId = :boardId
            """)
    Optional<Bookmark> findByUsernameAndBoardId(@Param("username") String username, @Param("boardId") Long boardId);


    @Query("""
            SELECT bm
            FROM Bookmark bm
            JOIN FETCH bm.member m
            JOIN FETCH bm.board b
            WHERE m.username = :username
            ORDER BY bm.createTime DESC
            """)
    Page<Bookmark> findByUsername(@Param("username") String username, Pageable pageable);


    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.board.boardId = :boardId")
    Long countByBoardId(@Param("boardId") Long boardId);


    @Query("""
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM Bookmark b
            JOIN b.member m
            JOIN b.board brd
            WHERE m.username = :username AND brd.boardId = :boardId
            """)
    boolean existsByUsernameAndBoardId(@Param("username") String username, @Param("boardId") Long boardId);


    @Query("""
            SELECT COUNT(b)
            FROM Bookmark b
            JOIN b.member m
            JOIN b.board brd
            WHERE m.username = :username
            """)
    Long countByUsername(@Param("username") String username);
}
