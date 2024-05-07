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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
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

        // 기본 테스트 데이터를 생성
        comment = GeneralComment.builder()
                .commentId("1")
                .content("Sample comment")
                .createdAt(LocalDateTime.now())
                .build();

        commentDto = GeneralCommentDto.builder()
                .commentId("1")
                .content("Sample comment")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void createComment_shouldSaveComment() {
        // Given
        when(mapper.toEntity(any(GeneralCommentDto.class))).thenReturn(comment);
        when(repository.save(any(GeneralComment.class))).thenReturn(comment);
        when(mapper.toDto(any(GeneralComment.class))).thenReturn(commentDto);

        // When
        GeneralCommentDto result = service.createComment(commentDto);

        // Then
        assertNotNull(result);
        verify(repository).save(comment);
        verify(mapper).toDto(comment);
    }

    @Test
    @DisplayName("댓글 조회 테스트")
    void getComment_shouldReturnComment_whenCommentExists() {
        // Given
        when(repository.findById("1")).thenReturn(Optional.of(comment));
        when(mapper.toDto(any(GeneralComment.class))).thenReturn(commentDto);

        // When
        GeneralCommentDto result = service.getComment("1");

        // Then
        assertNotNull(result);
        verify(repository).findById("1");
        verify(mapper).toDto(comment);
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void updateComment_shouldUpdateAndReturnComment_whenCommentExists() {
        // Given
        when(repository.findById(anyString())).thenReturn(Optional.of(comment));
        when(repository.save(any(GeneralComment.class))).thenReturn(comment);
        when(mapper.toEntity(any(GeneralCommentDto.class))).thenReturn(comment);
        when(mapper.toDto(any(GeneralComment.class))).thenReturn(commentDto);

        // When
        GeneralCommentDto result = service.updateComment(commentDto);

        // Then
        assertNotNull(result);
        verify(repository).save(comment);
        verify(mapper).toDto(comment);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteComment_shouldCallDeleteById() {
        // Given
        String commentId = "1";
        doNothing().when(repository).deleteById(commentId);

        // When
        service.deleteComment(commentId);

        // Then
        verify(repository).deleteById(commentId);
    }
}