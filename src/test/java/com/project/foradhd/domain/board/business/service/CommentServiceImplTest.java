package com.project.foradhd.domain.board.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

import java.util.Optional;

import com.project.foradhd.domain.board.business.service.Impl.CommentServiceImpl;
import com.project.foradhd.domain.board.fixtures.CommentFixtures;
import com.project.foradhd.domain.board.fixtures.PostFixtures;
import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.CommentLikeFilter;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.CommentLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.fixtures.UserFixtures;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    CommentRepository commentRepository;

    @Mock
    CommentLikeFilterRepository commentLikeFilterRepository;

    @Mock
    UserService userService;

    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    void shouldGetCommentById() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment comment = CommentFixtures.toComment(post, user).build();

        given(commentRepository.findByIdFetch(comment.getId())).willReturn(Optional.of(comment));

        //when
        Comment foundComment = commentService.getComment(comment.getId());

        //then
        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getId()).isEqualTo(comment.getId());
        assertThat(foundComment.getContent()).isEqualTo("테스트 댓글 내용");

        then(commentRepository).should(times(1)).findByIdFetch(comment.getId());
    }

    @Test
    void shouldCreateComment() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment comment = CommentFixtures.toComment(post, user).build();
        UserProfile userProfile = UserFixtures.toUserProfile().build();

        given(userService.getUserProfile(user.getId())).willReturn(userProfile);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        //when
        Comment createdComment = commentService.createComment(comment, user.getId());

        //then
        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getContent()).isEqualTo("테스트 댓글 내용");

        then(userService).should(times(1)).getUserProfile(user.getId());
        then(commentRepository).should(times(1)).save(any(Comment.class));
    }

    @Test
    void shouldDeleteComment() {
        //given
        Long commentId = 1L;
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment comment = CommentFixtures.toComment(post, user).build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        //when
        commentService.deleteComment(commentId);

        //then
        then(commentRepository).should(times(1)).findById(commentId);
        then(commentRepository).should(times(1)).detachChildComments(commentId);
        then(commentRepository).should(times(1)).deleteCommentById(commentId);
    }

    @Test
    void shouldUpdateComment() {
        //given
        Long commentId = 1L;
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment existingComment = CommentFixtures.toComment(post, user).build();
        Comment updatedComment = existingComment.toBuilder().content("수정된 댓글 내용").build();
        UserProfile userProfile = UserFixtures.toUserProfile().build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(existingComment));
        given(userService.getUserProfile(user.getId())).willReturn(userProfile);
        given(commentRepository.save(any(Comment.class))).willReturn(updatedComment);

        //when
        Comment result = commentService.updateComment(commentId, "수정된 댓글 내용", false, user.getId());

        //that
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("수정된 댓글 내용");

        then(commentRepository).should(times(1)).findById(commentId);
        then(userService).should(times(1)).getUserProfile(user.getId());
        then(commentRepository).should(times(1)).save(any(Comment.class));
    }

    @Test
    void shouldGetMyCommentedPosts() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment comment = CommentFixtures.toComment(post, user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentPage = new PageImpl<>(Arrays.asList(comment));

        given(commentRepository.findByUserId(eq(user.getId()), any(Pageable.class))).willReturn(commentPage);

        //when
        Page<PostResponseDto.PostListResponseDto> result = commentService.getMyCommentedPosts(user.getId(), pageable, SortOption.NEWEST_FIRST);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        then(commentRepository).should(times(1)).findByUserId(eq(user.getId()), any(Pageable.class));
    }

    @Test
    void shouldGetCommentsByPost() {
        //given
        Long postId = 1L;
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment comment = CommentFixtures.toComment(post, user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentPage = new PageImpl<>(Arrays.asList(comment));

        given(commentRepository.findByPostId(eq(postId), any(Pageable.class))).willReturn(commentPage);

        //when
        Page<Comment> result = commentService.getCommentsByPost(postId, pageable, SortOption.NEWEST_FIRST);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        then(commentRepository).should(times(1)).findByPostId(eq(postId), any(Pageable.class));
    }

    @Test
    void shouldToggleCommentLike() {
        //given
        Long commentId = 1L;
        String userId = "userId";
        CommentLikeFilter commentLikeFilter = CommentLikeFilter.builder()
                .comment(Comment.builder().id(commentId).build())
                .user(User.builder().id(userId).build())
                .build();

        given(commentLikeFilterRepository.findByCommentIdAndUserId(commentId, userId)).willReturn(Optional.of(commentLikeFilter));

        //when
        commentService.toggleCommentLike(commentId, userId);

        //then
        then(commentLikeFilterRepository).should(times(1)).findByCommentIdAndUserId(commentId, userId);
        then(commentLikeFilterRepository).should(times(1)).deleteByCommentIdAndUserId(commentId, userId);
        then(commentLikeFilterRepository).should(times(1)).decrementLikeCount(commentId);
    }
}
