package com.project.foradhd.domain.board.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.foradhd.domain.board.persistence.entity.GeneralComment;

import java.util.List;

@Repository
public interface GeneralCommentRepository extends JpaRepository<GeneralComment, Long> {

    // 특정 포스트에 대한 모든 댓글 조회
    List<GeneralComment> findByPostId(Long postId);

    // 부모 댓글 ID를 기준으로 모든 대댓글 조회
    List<GeneralComment> findByParentCommentId(Long parentCommentId);
}
