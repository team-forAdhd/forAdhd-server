package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.CommentLikeFilterService;
import com.project.foradhd.domain.board.business.service.CommentService;
import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.domain.board.web.dto.CommentDto;
import com.project.foradhd.domain.board.web.mapper.CommentMapper;
import com.project.foradhd.domain.board.business.service.CommentLikeFilterService;
import com.project.foradhd.global.exception.CommentNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final CommentLikeFilterService commentLikeFilterService;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, CommentMapper mapper, CommentLikeFilterService commentLikeFilterService) {
        this.repository = repository;
        this.mapper = mapper;
        this.commentLikeFilterService = commentLikeFilterService;
    }

    @Override
    public CommentDto getComment(Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with ID " + commentId + " not found"));
        return mapper.toDto(comment);
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        Comment comment = mapper.toEntity(commentDto);
        Comment savedComment = repository.save(comment);
        return mapper.toDto(savedComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!repository.existsById(commentId)) {
            throw new CommentNotFoundException("Comment not found with ID: " + commentId);
        }
        repository.deleteById(commentId);
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto) {
        Comment existingComment = repository.findById(commentDto.getCommentId())
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentDto.getCommentId()));
        mapper.updateCommentFromDto(commentDto, existingComment);
        Comment updatedComment = repository.save(existingComment);
        return mapper.toDto(updatedComment);
    }

    @Override
    public Page<CommentDto> getMyComments(Long writerId, Pageable pageable) {
        return repository.findByWriterId(writerId, pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<CommentDto> getCommentsByPost(Long postId, Pageable pageable, SortOption sortOption) {
        pageable = applySorting(pageable, sortOption);
        return repository.findByPostId(postId, pageable)
                .map(mapper::toDto);
    }

    @Override
    public void toggleCommentLike(Long userId, Long commentId) {
        commentLikeFilterService.toggleLike(userId, commentId);
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
