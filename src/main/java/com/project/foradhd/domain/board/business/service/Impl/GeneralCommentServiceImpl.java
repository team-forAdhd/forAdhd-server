package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.CommentLikeService;
import com.project.foradhd.domain.board.business.service.GeneralCommentService;
import com.project.foradhd.domain.board.persistence.entity.GeneralComment;
import com.project.foradhd.domain.board.persistence.repository.GeneralCommentRepository;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import com.project.foradhd.domain.board.web.mapper.GeneralCommentMapper;
import com.project.foradhd.global.exception.CommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
        repository.save(existingComment);
        return mapper.toDto(existingComment);
    }

    @Override
    public List<GeneralCommentDto> getUserComments(String userId, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "createdAt");
        List<GeneralComment> comments = repository.findByWriterId(userId, sort);
        return comments.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void toggleCommentLike(String userId, String commentId) {
        commentLikeService.toggleLike(userId, commentId);
    }
}
