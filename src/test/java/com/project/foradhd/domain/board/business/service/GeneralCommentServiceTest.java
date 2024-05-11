package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.business.service.Impl.GeneralCommentServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.GeneralComment;
import com.project.foradhd.domain.board.persistence.repository.GeneralCommentRepository;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import com.project.foradhd.domain.board.web.mapper.GeneralCommentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GeneralCommentServiceTest {

    @Mock
    private GeneralCommentRepository repository;

    @Mock
    @Autowired
    private GeneralCommentMapper mapper;

    @InjectMocks
    private GeneralCommentServiceImpl service;

    GeneralComment comment;
    GeneralCommentDto commentDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        LocalDateTime now = LocalDateTime.now();
        mapper = Mappers.getMapper(GeneralCommentMapper.class);

        GeneralComment comment = new GeneralComment(
                "1",
                "user123",
                "writer123",
                "John Doe",
                "post123",
                1,
                "parent1",
                "Sample content",
                false,
                5,
                now,
                now
        );

        GeneralCommentDto commentDto = new GeneralCommentDto(comment.getCommentId(), comment.getUserId(), comment.getWriterId(), comment.getWriterName(), comment.getPostId(), comment.getPostType(), comment.getParentCommentId(), comment.getContent(), comment.isAnonymous(), comment.getLikeCount(), comment.getCreatedAt(), comment.getLastModifiedAt());
    }


    @Test
    @DisplayName("댓글 작성 테스트")
    void testCreateComment() {
        LocalDateTime now = LocalDateTime.now();
        GeneralComment comment = new GeneralComment(
            "1",
            "user123",
            "writer123",
            "John Doe",
            "post123",
            1,
            "parent1",
            "Sample content",
            false,
            5,
            now,
            now
    );
        GeneralCommentDto commentDto = new GeneralCommentDto(comment.getCommentId(), comment.getUserId(), comment.getWriterId(), comment.getWriterName(), comment.getPostId(), comment.getPostType(), comment.getParentCommentId(), comment.getContent(), comment.isAnonymous(), comment.getLikeCount(), comment.getCreatedAt(), comment.getLastModifiedAt());

        repository.save(comment);
        GeneralCommentDto savedDto = service.createComment(commentDto);

        // Assert
        assertNotNull(savedDto, "The saved comment DTO should not be null.");
        assertEquals(commentDto.getContent(), savedDto.getContent(), "The content of the saved comment should match the input.");
    }


    @Test
    @DisplayName("댓글 조회 테스트")
    void testGetComment() {
        // Setup
        String commentId = comment.getCommentId();
        when(repository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        GeneralCommentDto resultDto = service.getComment(commentId);

        // Assert
        assertNotNull(resultDto, "The result comment DTO should not be null.");
        assertEquals(comment.getContent(), resultDto.getContent(), "The content of the comment should match the expected.");
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void testUpdateComment() {
        // Given
        GeneralCommentDto updatedDto = GeneralCommentDto.builder()
                .commentId(commentDto.getCommentId())
                .content("Updated content")
                .build();
        when(repository.findById(comment.getCommentId())).thenReturn(Optional.of(comment));
        when(repository.save(comment)).thenReturn(comment);

        // Act
        GeneralCommentDto result = service.updateComment(updatedDto);

        // Assert
        assertNotNull(result, "The updated comment DTO should not be null.");
        assertEquals("Updated content", result.getContent(), "The content should be updated.");
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void testDeleteComment() {
        // Given
        String commentId = comment.getCommentId();
        //when(repository.existsById(commentId)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> service.deleteComment(commentId), "Deleting a comment should not throw any exceptions.");
    }
}