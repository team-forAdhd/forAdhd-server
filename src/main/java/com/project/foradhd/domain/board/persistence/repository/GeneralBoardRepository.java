package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralBoardRepository extends JpaRepository<GeneralPost, String>{
    List<GeneralPost> findScrapedByUserId(String userId);
    List<GeneralPost> findByWriterId(String writerId);

    // 최신순 정렬
    @Query("SELECT p FROM GeneralPost p ORDER BY p.postId DESC")
    List<GeneralPost> findAllOrderByPostIdDesc();

    // 오래된 순 정렬
    @Query("SELECT p FROM GeneralPost p ORDER BY p.postId ASC")
    List<GeneralPost> findAllOrderByPostIdAsc();

    List<GeneralPost> findByCategoryId(String categoryId);

    // 게시글 좋아요 기능
    @Modifying
    @Query("UPDATE GeneralPost p SET p.likeCount = p.likeCount + 1 WHERE p.postId = :postId")
    void incrementLikeCount(String postId);

    @Modifying
    @Query("UPDATE GeneralPost p SET p.likeCount = p.likeCount - 1 WHERE p.postId = :postId")
    void decrementLikeCount(String postId);
}
