package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.GeneralCommentService;
import com.project.foradhd.domain.board.persistence.entity.GeneralComment;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.repository.GeneralCommentRepository;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.mapper.GeneralCommentMapper;
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

    @Autowired
    public GeneralCommentServiceImpl(GeneralCommentRepository repository, GeneralCommentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GeneralCommentDto getComment(String commentId) {
        GeneralComment comment = repository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
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
        repository.deleteById(commentId);
    }

    @Override
    public GeneralCommentDto updateComment(GeneralCommentDto commentDto) {
        GeneralComment existingComment = repository.findById(commentDto.getCommentId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        existingComment = mapper.toEntity(commentDto);
        repository.save(existingComment);
        return mapper.toDto(existingComment);
    }

    @Override
    public List<GeneralCommentDto> listComments() {
        List<GeneralComment> comments = repository.findAll();
        return comments.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GeneralCommentDto> listCommentsByPostId(String postId) {
        List<GeneralComment> comments = repository.findByPostId(postId);
        return comments.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GeneralCommentDto> getUserComments(String userId, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "createdAt");
        List<GeneralComment> comments = repository.findByWriterId(userId, sort);
        return comments.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
