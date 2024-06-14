package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostLikeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeFilterRepository extends JpaRepository<PostLikeFilter, Long> {
    boolean existsByUserIdAndPostId(String userId, Long postId);
    void deleteByUserIdAndPostId(String userId, Long postId);
    @Query("SELECT plf.post FROM PostLikeFilter plf WHERE plf.user.id = :userId")
    Page<Post> findPostsLikedByUser(@Param("userId") String userId, Pageable pageable);
}
