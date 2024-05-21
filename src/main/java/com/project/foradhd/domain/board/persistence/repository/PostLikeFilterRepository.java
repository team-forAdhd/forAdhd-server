package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.PostLikeFilter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeFilterRepository extends JpaRepository<PostLikeFilter, Long> {
    boolean existsByUserIdAndPostId(String userId, Long postId);
    void deleteByUserIdAndPostId(String userId, Long postId);
}
