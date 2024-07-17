package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.CommentService;
import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.CommentLikeFilter;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.CommentLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static com.project.foradhd.global.exception.ErrorCode.NOT_FOUND_COMMENT;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final CommentLikeFilterRepository commentLikeFilterRepository;

    @Override
    public Comment getComment(Long commentId) {
        return repository.findById(commentId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_COMMENT));
    }

    @Override
    @Transactional
    public Comment createComment(Comment comment) {
        return repository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_COMMENT));

        // 원 댓글에 연결된 대댓글의 부모 ID를 null로 설정
        for (Comment childComment : comment.getChildComments()) {
            childComment.setParentComment(null);
            repository.save(childComment);
        }
        // 원 댓글 삭제
        repository.delete(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(Comment comment) {
        Comment existingComment = repository.findById(comment.getId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_COMMENT));
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
