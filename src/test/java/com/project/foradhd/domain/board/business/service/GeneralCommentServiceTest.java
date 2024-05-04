package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.business.service.Impl.GeneralCommentServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.GeneralComment;
import com.project.foradhd.domain.board.persistence.repository.GeneralCommentRepository;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import com.project.foradhd.domain.board.web.mapper.GeneralCommentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GeneralCommentServiceTest {

    @Mock
    private GeneralCommentRepository repository;
    @Mock
    private GeneralCommentMapper mapper;

    @InjectMocks
    private GeneralCommentServiceImpl service;

    private GeneralComment comment;
    private GeneralCommentDto commentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        comment = new GeneralComment();
        commentDto = new GeneralCommentDto();

        comment.setId("testId");
        comment.setContent("Sample comment");
    }

    @Test
    void getComment_shouldReturnComment_whenCommentExists() {
        GeneralCommentDto result = service.getComment("testId");

        when(repository.findById(anyString())).thenReturn(Optional.of(comment));
        when(mapper.toDto(any(GeneralComment.class))).thenReturn(commentDto);

        assertNotNull(result);
        verify(repository).findById("testId");
        verify(mapper).toDto(comment);
    }

    @Test
    void createComment_shouldSaveComment() {
        when(mapper.toEntity(any(GeneralCommentDto.class))).thenReturn(comment);
        when(repository.save(any(GeneralComment.class))).thenReturn(comment);
        when(mapper.toDto(any(GeneralComment.class))).thenReturn(commentDto);

        GeneralCommentDto result = service.createComment(commentDto);
        assertNotNull(result);
        verify(repository).save(comment);
        verify(mapper).toDto(comment);
    }

    @Test
    void updateComment_shouldUpdateAndReturnComment_whenCommentExists() {
        when(repository.findById(anyString())).thenReturn(Optional.of(comment));
        when(repository.save(any(GeneralComment.class))).thenReturn(comment);
        when(mapper.toEntity(any(GeneralCommentDto.class))).thenReturn(comment);
        when(mapper.toDto(any(GeneralComment.class))).thenReturn(commentDto);

        GeneralCommentDto result = service.updateComment(commentDto);
        assertNotNull(result);
        verify(repository).save(comment);
        verify(mapper).toDto(comment);
    }

    @Test
    void deleteComment_shouldCallDeleteById() {
        String Id = "1";
        doNothing().doThrow(new RuntimeException()).when(repository).deleteById(Id);
        service.deleteComment(Id);
        repository.deleteById(Id);
        verify(repository).deleteById(Id);
    }
}