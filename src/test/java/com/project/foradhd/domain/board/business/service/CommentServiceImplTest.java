package com.project.foradhd.domain.board.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import com.project.foradhd.domain.board.business.service.Impl.CommentServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.repository.CommentLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeFilterRepository commentLikeFilterRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment comment;
    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id("user1").build();
        post = Post.builder().id(1L).title("Post Title").content("Post Content").build();
        comment = Comment.builder().id(1L).content("Comment Content").post(post).user(user).build();
    }

    @Test
    void shouldDeleteComment() {
        // given
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        // when
        commentService.deleteComment(comment.getId());

        // then
        then(commentRepository).should(times(1)).deleteById(comment.getId());
        then(commentRepository).should(times(1)).findById(comment.getId());
    }

    @Test
    void shouldUpdateComment() {
        // given
        String updatedContent = "Updated Comment Content";
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Comment updatedComment = commentService.updateComment(comment.getId(), updatedContent);

        // then
        assertThat(updatedComment.getContent()).isEqualTo(updatedContent);
        then(commentRepository).should(times(1)).save(any(Comment.class));
    }

    @Test
    void shouldGetMyCommentedPosts() {
        // given
        Page<Comment> commentsPage = new PageImpl<>(List.of(comment), PageRequest.of(0, 10), 1);
        given(commentRepository.findByUserId(eq(user.getId()), any(Pageable.class))).willReturn(commentsPage);

        // when
        Page<PostResponseDto> postsPage = commentService.getMyCommentedPosts(user.getId(), PageRequest.of(0, 10));

        // then
        assertThat(postsPage.getContent()).hasSize(1);
        assertThat(postsPage.getContent().get(0).getId()).isEqualTo(post.getId());
        then(commentRepository).should(times(1)).findByUserId(eq(user.getId()), any(Pageable.class));
    }
}