package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface GeneralBoardRepository extends JpaRepository<GeneralPost, String> {
    Page<GeneralPost> findByWriterId(String writerId, Pageable pageable);
    Page<GeneralPost> findByCategoryId(String categoryId, Pageable pageable);
    Page<GeneralPost> findByUserId(String userId, Pageable pageable);

    // 게시글 좋아요 기능
    @Modifying
    @Query("UPDATE GeneralPost p SET p.likeCount = p.likeCount + 1 WHERE p.postId = :postId")
    void incrementLikeCount(String postId);

    @Modifying
    @Query("UPDATE GeneralPost p SET p.likeCount = p.likeCount - 1 WHERE p.postId = :postId")
    void decrementLikeCount(String postId);
}
