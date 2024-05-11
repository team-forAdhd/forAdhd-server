package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByWriterId(Long writerId, Pageable pageable);
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + 1 WHERE c.commentId = :commentId")
    void incrementLikeCount(Long commentId);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount - 1 WHERE c.commentId = :commentId")
    void decrementLikeCount(Long commentId);

    @Modifying
    @Query("UPDATE Comment c SET c.content = :content WHERE c.commentId = :commentId")
    void updateContent(Long commentId, String content);
}
