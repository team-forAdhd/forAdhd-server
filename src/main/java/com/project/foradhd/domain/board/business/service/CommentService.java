package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Comment getComment(Long commentId);
    Comment createComment(Comment comment);
    void deleteComment(Long commentId);
    Comment updateComment(Comment comment);
    Page<Comment> getMyComments(Long writerId, Pageable pageable);
    Page<Comment> getCommentsByPost(Long postId, Pageable pageable, SortOption sortOption);
    void toggleCommentLike(Long commentId, String userId);
}
