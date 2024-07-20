package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId")
    Page<Comment> findByUserId(@Param("userId") String userId, Pageable pageable);

    @Modifying
    @Query("delete Comment c where c.id = :commentId")
    void deleteById(@Param("commentId") Long commentId);

    @Modifying
    @Query("delete Comment c where c.parentComment.id = :parentId")
    void deleteByParentId(@Param("parentId") Long parentId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.parentComment.id = :parentId")
    int countByParentCommentId(@Param("parentId") Long parentId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    long countByPostId(@Param("postId") Long postId);

    List<Comment> findByParentCommentId(Long parentCommentId);


}
