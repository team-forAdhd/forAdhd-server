package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.request.CreateCommentRequestDto;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Comment getComment(Long commentId);
    Comment createComment(Comment comment, String userId);
    void deleteComment(Long commentId);
    void deleteChildrenComment(Long commentId);
    Comment updateComment(Long commentId, String content);
    Page<PostResponseDto> getMyCommentedPosts(String userId, Pageable pageable);
    Page<Comment> getCommentsByPost(Long postId, Pageable pageable, SortOption sortOption);
    void toggleCommentLike(Long commentId, String userId);
}
