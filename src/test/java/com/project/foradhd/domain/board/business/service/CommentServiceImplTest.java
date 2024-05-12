package com.project.foradhd.domain.board.business.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.foradhd.domain.board.business.service.Impl.CommentServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.domain.board.web.dto.CommentDto;
import com.project.foradhd.domain.board.web.mapper.CommentMapper;
import com.project.foradhd.global.exception.CommentNotFoundException;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@DisplayName("댓글 서비스 테스트")
class CommentServiceImplTest {

    @Mock
    private CommentRepository repository;

    @Mock
    private CommentMapper mapper;

    @Mock
    private CommentLikeFilterService commentLikeFilterService;

    @InjectMocks
    private CommentServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("댓글 조회 - 댓글이 존재하는 경우")
    void getComment_exist() {
        Long commentId = 1L;
        Comment comment = Comment.builder()
                .commentId(commentId)
                .content("Sample comment")
                .createdAt(LocalDateTime.now())
                .build();
        CommentDto commentDto = CommentDto.builder()
                .commentId(commentId)
                .content("Sample comment")
                .createdAt(comment.getCreatedAt())
                .build();

        when(repository.findById(commentId)).thenReturn(Optional.of(comment));
        when(mapper.toDto(comment)).thenReturn(commentDto);

        CommentDto result = service.getComment(commentId);

        assertEquals(commentDto, result);
        verify(repository).findById(commentId);
        verify(mapper).toDto(comment);
    }

    @Test
    @DisplayName("댓글 조회 - 댓글이 존재하지 않는 경우")
    void getComment_notexist() {
        Long commentId = 1L;
        when(repository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> service.getComment(commentId));
    }

    @Test
    @DisplayName("댓글 생성")
    void createComment() {
        CommentDto commentDto = CommentDto.builder()
                .content("New comment")
                .createdAt(LocalDateTime.now())
                .build();
        Comment comment = Comment.builder()
                .content("New comment")
                .createdAt(commentDto.getCreatedAt())
                .build();
        Comment savedComment = Comment.builder()
                .commentId(1L)
                .content("New comment")
                .createdAt(LocalDateTime.now())
                .build();

        when(mapper.toEntity(commentDto)).thenReturn(comment);
        when(repository.save(comment)).thenReturn(savedComment);
        when(mapper.toDto(savedComment)).thenReturn(commentDto);

        CommentDto result = service.createComment(commentDto);

        assertEquals(commentDto, result);
        verify(repository).save(comment);
        verify(mapper).toDto(savedComment);
    }

    @Test
    @DisplayName("존재하는 댓글 삭제")
    void deleteComment_exists() {
        Long commentId = 1L;
        when(repository.existsById(commentId)).thenReturn(true);

        service.deleteComment(commentId);

        verify(repository).deleteById(commentId);
    }

    @Test
    @DisplayName("존재하지 않는 댓글 삭제")
    void deleteComment_notExists() {
        Long commentId = 1L;
        when(repository.existsById(commentId)).thenReturn(false);

        assertThrows(CommentNotFoundException.class, () -> service.deleteComment(commentId));
    }

    @Test
    @DisplayName("나의 댓글 조회")
    void getMyComments() {
        Long writerId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Comment comment = Comment.builder()
                .writerId("writer1")
                .content("My comment")
                .build();
        Page<Comment> page = new PageImpl<>(Collections.singletonList(comment));

        when(repository.findByWriterId(writerId, pageable)).thenReturn(page);
        when(mapper.toDto(any(Comment.class))).thenReturn(CommentDto.builder()
                .content("My comment")
                .build());

        Page<CommentDto> result = service.getMyComments(writerId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(repository).findByWriterId(writerId, pageable);
        verify(mapper, times(1)).toDto(any(Comment.class));
    }

    @Test
    @DisplayName("게시글에 대한 댓글 조회")
    void getCommentsByPost() {
        Long postId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        
        GeneralPost post = GeneralPost.builder()
                .postId(postId)
                .build();

        Comment comment = Comment.builder()
                .postId(post)
                .content("Post comment")
                .build();
        Page<Comment> page = new PageImpl<>(Collections.singletonList(comment));

        when(repository.findByPostId(postId, pageable)).thenReturn(page);
        when(mapper.toDto(any(Comment.class))).thenReturn(CommentDto.builder()
                .content("Post comment")
                .build());

        Page<CommentDto> result = service.getCommentsByPost(postId, pageable, SortOption.NEWEST_FIRST);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(repository).findByPostId(postId, pageable);
        verify(mapper, times(1)).toDto(any(Comment.class));
    }

}
