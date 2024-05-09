package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, String> {
    Optional<CommentLike> findByUserIdAndCommentId(String userId, String commentId);
    boolean existsByUserIdAndCommentId(String userId, String commentId);
    void deleteByUserIdAndCommentId(String userId, String commentId);
}
