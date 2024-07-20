package com.project.foradhd.domain.board.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import com.project.foradhd.domain.board.business.service.Impl.CommentServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.CommentLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeFilterRepository commentLikeFilterRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void testGetComment() {
        // given
        Long commentId = 1L;
        Comment mockComment = new Comment();
        mockComment.setId(commentId);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(mockComment));

        // when
        Comment foundComment = commentService.getComment(commentId);

        // then
        assertThat(foundComment.getId()).isEqualTo(commentId);
    }

    @Test
    void testCreateComment() {
        // given
        Comment newComment = new Comment();
        newComment.setContent("New Comment");
        given(commentRepository.save(any(Comment.class))).willReturn(newComment);

        // when
        Comment savedComment = commentService.createComment(newComment);

        // then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("New Comment");
    }

    @Test
    void testGetCommentsByPost() {
        // given
        Long postId = 1L;
        SortOption sortOption = SortOption.NEWEST_FIRST;
        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());
        Page<Comment> expectedPage = new PageImpl<>(comments, pageable, comments.size());
        given(commentRepository.findByPostId(eq(postId), any(Pageable.class))).willReturn(expectedPage);

        // when
        Page<Comment> resultPage = commentService.getCommentsByPost(postId, pageable, sortOption);

        // then
        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getContent()).hasSize(2);
    }
}
