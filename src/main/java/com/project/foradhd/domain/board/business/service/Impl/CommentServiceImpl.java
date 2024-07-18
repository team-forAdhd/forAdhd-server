package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.CommentService;
import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.CommentLikeFilter;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.CommentLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.foradhd.global.exception.ErrorCode.NOT_FOUND_COMMENT;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeFilterRepository commentLikeFilterRepository;

    @Override
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_COMMENT));
    }

    @Override
    @Transactional
    public Comment createComment(Comment comment, String userId) {
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = getComment(commentId);

        // 원 댓글에 연결된 대댓글의 부모 ID를 null로 설정
        for (Comment childComment : comment.getChildComments()) {
            Comment updatedChildComment = Comment.builder()
                    .id(childComment.getId())
                    .post(childComment.getPost())
                    .user(childComment.getUser())
                    .content(childComment.getContent())
                    .anonymous(childComment.isAnonymous())
                    .likeCount(childComment.getLikeCount())
                    .parentComment(null)
                    .build();
            commentRepository.save(updatedChildComment);
        }

        // 원 댓글 삭제
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void deleteChildrenComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, String content) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_COMMENT));
        Comment updatedComment = Comment.builder()
                .id(existingComment.getId())
                .post(existingComment.getPost())
                .user(existingComment.getUser())
                .content(content)
                .anonymous(existingComment.isAnonymous())
                .likeCount(existingComment.getLikeCount())
                .parentComment(existingComment.getParentComment())
                .childComments(existingComment.getChildComments())
                .build();
        return commentRepository.save(updatedComment);
    }

    @Override
    public Page<Comment> getMyComments(String userId, Pageable pageable) {
        Page<Comment> userComments = commentRepository.findByUserId(userId, pageable);
        List<Post> posts = userComments.stream()
                .map(Comment::getPost)
                .distinct()
                .collect(Collectors.toList());
        return new PageImpl<>(posts, pageable, posts.size());
    }

    @Override
    public Page<Comment> getCommentsByPost(Long postId, Pageable pageable, SortOption sortOption) {
        pageable = applySorting(pageable, sortOption);
        return commentRepository.findByPostId(postId, pageable);
    }

    @Override
    public void toggleCommentLike(Long commentId, String userId) {
        Optional<CommentLikeFilter> likeFilter = commentLikeFilterRepository.findByCommentIdAndUserId(commentId, userId);
        if (likeFilter.isPresent()) {
            commentLikeFilterRepository.deleteByCommentIdAndUserId(commentId, userId);
            commentLikeFilterRepository.decrementLikeCount(commentId);
        } else {
            commentLikeFilterRepository.incrementLikeCount(commentId);
        }
    }

    private Pageable applySorting(Pageable pageable, SortOption sortOption) {
        Sort sort = switch (sortOption) {
            case OLDEST_FIRST -> Sort.by(Sort.Direction.ASC, "createdAt");
            case NEWEST_FIRST, MOST_LIKED, MOST_VIEWED -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.unsorted();
        };
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
