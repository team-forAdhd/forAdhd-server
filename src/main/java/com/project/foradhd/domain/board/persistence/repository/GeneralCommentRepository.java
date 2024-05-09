package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.project.foradhd.domain.board.persistence.entity.GeneralComment;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Optional;

@Repository
public interface GeneralCommentRepository extends JpaRepository<GeneralComment, String> {
    Page<GeneralComment> findByWriterId(String writerId, Pageable pageable);
    Page<GeneralComment> findByPostId(String postId, Pageable pageable);

    @Modifying
    @Query("UPDATE GeneralComment c SET c.likeCount = c.likeCount + 1 WHERE c.commentId = :commentId")
    void incrementLikeCount(String commentId);

    @Modifying
    @Query("UPDATE GeneralComment c SET c.likeCount = c.likeCount - 1 WHERE c.commentId = :commentId")
    void decrementLikeCount(String commentId);
}
