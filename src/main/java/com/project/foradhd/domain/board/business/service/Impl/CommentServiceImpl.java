package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.CommentService;
import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.CommentLikeFilter;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.CommentLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final CommentLikeFilterRepository commentLikeFilterRepository;

    @Override
    public Comment getComment(Long commentId) {
        return repository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with ID " + commentId + " not found"));
    }

    @Override
    @Transactional
    public Comment createComment(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!repository.existsById(commentId)) {
            throw new CommentNotFoundException("Comment not found with ID: " + commentId);
        }
        repository.deleteById(commentId);
    }

    @Override
    @Transactional
    public Comment updateComment(Comment comment) {
        Comment existingComment = repository.findById(comment.getId())
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + comment.getId()));
        existingComment.setContent(comment.getContent());
        return repository.save(existingComment);
    }

    @Override
    public Page<Comment> getMyComments(Long writerId, Pageable pageable) {
        return repository.findByWriterId(writerId, pageable);
    }

    @Override
    public Page<Comment> getCommentsByPost(Long postId, Pageable pageable, SortOption sortOption) {
        pageable = applySorting(pageable, sortOption);
        return repository.findByPostId(postId, pageable);
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
