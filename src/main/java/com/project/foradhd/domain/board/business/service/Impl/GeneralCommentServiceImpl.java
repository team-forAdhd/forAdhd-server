package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.CommentLikeService;
import com.project.foradhd.domain.board.business.service.GeneralCommentService;
import com.project.foradhd.domain.board.persistence.entity.GeneralComment;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.GeneralCommentRepository;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import com.project.foradhd.domain.board.web.mapper.GeneralCommentMapper;
import com.project.foradhd.global.exception.CommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GeneralCommentServiceImpl implements GeneralCommentService {

    private final GeneralCommentRepository repository;
    private final GeneralCommentMapper mapper;
    private final CommentLikeService commentLikeService;

    @Autowired
    public GeneralCommentServiceImpl(GeneralCommentRepository repository, GeneralCommentMapper mapper, CommentLikeService commentLikeService) {
        this.repository = repository;
        this.mapper = mapper;
        this.commentLikeService = commentLikeService;
    }

    @Override
    public GeneralCommentDto getComment(String commentId) {
        GeneralComment comment = repository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));
        return mapper.toDto(comment);
    }

    @Override
    public GeneralCommentDto createComment(GeneralCommentDto commentDto) {
        GeneralComment comment = mapper.toEntity(commentDto);
        comment = repository.save(comment);
        return mapper.toDto(comment);
    }

    @Override
    public void deleteComment(String commentId) {
        if (!repository.existsById(commentId)) {
            throw new CommentNotFoundException("Comment not found with ID: " + commentId);
        }
        repository.deleteById(commentId);
    }

    @Override
    public GeneralCommentDto updateComment(GeneralCommentDto commentDto) {
        GeneralComment existingComment = repository.findById(commentDto.getCommentId())
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentDto.getCommentId()));
        existingComment = mapper.toEntity(commentDto);
        existingComment = repository.save(existingComment);
        return mapper.toDto(existingComment);
    }

    private Pageable applySorting(Pageable pageable, SortOption sortOption) {
        Sort sort;
        switch (sortOption) {
            case OLDEST_FIRST:
                sort = Sort.by(Sort.Direction.ASC, "createdAt");
                break;
            case NEWEST_FIRST:
            default:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    @Override
    public Page<GeneralCommentDto> getMyComments(String writerId, Pageable pageable) {
        return repository.findByWriterId(writerId, pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<GeneralCommentDto> getCommentsByPost(String postId, Pageable pageable, SortOption sortOption) {
        pageable = applySorting(pageable, sortOption);
        return repository.findByPostId(postId, pageable)
                .map(mapper::toDto);
    }

    @Override
    public void toggleCommentLike(String userId, String commentId) {
        commentLikeService.toggleLike(userId, commentId);
    }
}
