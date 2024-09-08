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
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
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

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeFilterRepository commentLikeFilterRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void shouldGetCommentById() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment comment = CommentFixtures.toComment(post, user).build();

        given(commentRepository.findByIdFetch(comment.getId())).willReturn(Optional.of(comment));

        Comment foundComment = commentService.getComment(comment.getId());

        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getId()).isEqualTo(comment.getId());
        assertThat(foundComment.getContent()).isEqualTo("테스트 댓글 내용");

        then(commentRepository).should(times(1)).findByIdFetch(comment.getId());
    }

    @Test
    public void shouldCreateComment() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment comment = CommentFixtures.toComment(post, user).build();
        UserProfile userProfile = UserFixtures.toUserProfile().build();

        given(userService.getUserProfile(user.getId())).willReturn(userProfile);
        given(commentRepository.save(comment)).willReturn(comment);

        Comment createdComment = commentService.createComment(comment, user.getId());

        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getContent()).isEqualTo("테스트 댓글 내용");

        then(userService).should(times(1)).getUserProfile(user.getId());
        then(commentRepository).should(times(1)).save(comment);
    }

    @Test
    public void shouldDeleteComment() {
        Long commentId = 1L;
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment comment = CommentFixtures.toComment(post, user).build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        commentService.deleteComment(commentId);

        then(commentRepository).should(times(1)).findById(commentId);
        then(commentRepository).should(times(1)).detachChildComments(commentId);
        then(commentRepository).should(times(1)).deleteCommentById(commentId);
    }

    @Test
    public void shouldUpdateComment() {
        Long commentId = 1L;
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment existingComment = CommentFixtures.toComment(post, user).build();
        Comment updatedComment = existingComment.toBuilder().content("수정된 댓글 내용").build();
        UserProfile userProfile = UserFixtures.toUserProfile().build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(existingComment));
        given(userService.getUserProfile(user.getId())).willReturn(userProfile);
        given(commentRepository.save(updatedComment)).willReturn(updatedComment);

        Comment result = commentService.updateComment(commentId, "수정된 댓글 내용", false, user.getId());

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("수정된 댓글 내용");

        then(commentRepository).should(times(1)).findById(commentId);
        then(userService).should(times(1)).getUserProfile(user.getId());
        then(commentRepository).should(times(1)).save(updatedComment);
    }

    @Test
    public void shouldGetMyCommentedPosts() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment comment = CommentFixtures.toComment(post, user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentPage = new PageImpl<>(Arrays.asList(comment));

        given(commentRepository.findByUserId(user.getId(), pageable)).willReturn(commentPage);

        Page<PostResponseDto.PostListResponseDto> result = commentService.getMyCommentedPosts(user.getId(), pageable, SortOption.NEWEST_FIRST);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        then(commentRepository).should(times(1)).findByUserId(user.getId(), pageable);
    }

    @Test
    public void shouldGetCommentsByPost() {
        Long postId = 1L;
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Comment comment = CommentFixtures.toComment(post, user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentPage = new PageImpl<>(Arrays.asList(comment));

        given(commentRepository.findByPostId(postId, pageable)).willReturn(commentPage);

        Page<Comment> result = commentService.getCommentsByPost(postId, pageable, SortOption.NEWEST_FIRST);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        then(commentRepository).should(times(1)).findByPostId(postId, pageable);
    }

    @Test
    public void shouldToggleCommentLike() {
        Long commentId = 1L;
        String userId = "user-id";
        CommentLikeFilter commentLikeFilter = CommentLikeFilter.builder()
                .comment(Comment.builder().id(commentId).build())
                .user(User.builder().id(userId).build())
                .build();

        given(commentLikeFilterRepository.findByCommentIdAndUserId(commentId, userId)).willReturn(Optional.of(commentLikeFilter));

        commentService.toggleCommentLike(commentId, userId);

        then(commentLikeFilterRepository).should(times(1)).findByCommentIdAndUserId(commentId, userId);
        then(commentLikeFilterRepository).should(times(1)).deleteByCommentIdAndUserId(commentId, userId);
        then(commentLikeFilterRepository).should(times(1)).decrementLikeCount(commentId);
    }
}
