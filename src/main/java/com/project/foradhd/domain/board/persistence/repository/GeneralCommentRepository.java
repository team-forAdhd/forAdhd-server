package com.project.foradhd.domain.board.persistence.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.project.foradhd.domain.board.persistence.entity.GeneralComment;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Optional;

@Repository
public interface GeneralCommentRepository extends JpaRepository<GeneralComment, String> {

    // 특정 포스트에 대한 모든 댓글 조회
    List<GeneralComment> findByPostId(String postId);

    // 부모 댓글 ID를 기준으로 모든 대댓글 조회
    List<GeneralComment> findByParentCommentId(String parentCommentId);

    List<GeneralComment> findByWriterId(String writerId, Sort sort);

    @Modifying
    @Query("UPDATE GeneralComment c SET c.likeCount = c.likeCount + 1 WHERE c.commentId = :commentId")
    void incrementLikeCount(String commentId);

    @Modifying
    @Query("UPDATE GeneralComment c SET c.likeCount = c.likeCount - 1 WHERE c.commentId = :commentId")
    void decrementLikeCount(String commentId);
}
