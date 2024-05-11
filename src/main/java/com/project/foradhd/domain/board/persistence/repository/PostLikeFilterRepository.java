package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.PostLikeFilter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeFilterRepository extends JpaRepository<PostLikeFilter, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
