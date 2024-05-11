package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.CommentLikeFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeFilterRepository extends JpaRepository<CommentLikeFilter, Long> {
    Optional<CommentLikeFilter> findByUserIdAndCommentId(Long userId, Long commentId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
    void deleteByUserIdAndCommentId(Long userId, Long commentId);
}
