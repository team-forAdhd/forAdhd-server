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
import com.project.foradhd.domain.board.persistence.entity.CommentLikeFilter;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.CommentLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
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

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeFilterRepository commentLikeFilterRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment comment;
    private UserProfile userProfile;
    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = User.builder().id("user123").build();
        post = Post.builder().id(1L).build();
        comment = Comment.builder()
                .id(1L)
                .user(user)
                .post(post)
                .content("Test comment")
                .anonymous(false)
                .likeCount(0)
                .nickname("TestUser")
                .profileImage("http://example.com/profile.png")
                .build();

        userProfile = UserProfile.builder()
                .user(user)
                .nickname("TestUser")
                .profileImage("http://example.com/profile.png")
                .build();
    }

    @Test
    void getComment_ShouldReturnCommentWithChildComments() {
        // given
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        given(commentRepository.countByParentCommentId(comment.getId())).willReturn(1);
        given(commentRepository.findByParentCommentId(comment.getId())).willReturn(Collections.singletonList(comment));

        // when
        Comment result = commentService.getComment(comment.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getChildComments()).hasSize(1);
    }

    @Test
    void createComment_ShouldSaveComment() {
        // given
        given(userProfileRepository.findByUserId(user.getId())).willReturn(Optional.of(userProfile));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        Comment result = commentService.createComment(comment, user.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNickname()).isEqualTo(userProfile.getNickname());
    }

    @Test
    void deleteComment_ShouldDeleteCommentAndUnsetParentForChildComments() {
        // given
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        given(commentRepository.findByParentCommentId(comment.getId())).willReturn(Collections.singletonList(comment));

        // when
        commentService.deleteComment(comment.getId());

        // then
        then(commentRepository).should().deleteById(comment.getId());
    }

    @Test
    void updateComment_ShouldUpdateCommentContent() {
        // given
        String newContent = "Updated content";
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        given(commentRepository.save(any(Comment.class))).willReturn(comment.toBuilder().content(newContent).build());

        // when
        Comment result = commentService.updateComment(comment.getId(), newContent);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(newContent);
    }

    @Test
    void getMyCommentedPosts_ShouldReturnPagedPosts() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(comment), pageable, 1);
        given(commentRepository.findByUserId(user.getId(), pageable)).willReturn(commentPage);

        // when
        Page<PostResponseDto.PostListResponseDto> result = commentService.getMyCommentedPosts(user.getId(), pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void getCommentsByPost_ShouldReturnPagedComments() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(comment), pageable, 1);
        given(commentRepository.findByPostId(1L, pageable)).willReturn(commentPage);

        // when
        Page<Comment> result = commentService.getCommentsByPost(1L, pageable, SortOption.NEWEST_FIRST);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void toggleCommentLike_ShouldIncrementOrDecrementLikeCount() {
        // given
        given(commentLikeFilterRepository.findByCommentIdAndUserId(comment.getId(), user.getId())).willReturn(Optional.empty());

        // when
        commentService.toggleCommentLike(comment.getId(), user.getId());

        // then
        then(commentLikeFilterRepository).should().save(any(CommentLikeFilter.class));
        then(commentLikeFilterRepository).should().incrementLikeCount(comment.getId());

        // given
        given(commentLikeFilterRepository.findByCommentIdAndUserId(comment.getId(), user.getId())).willReturn(Optional.of(CommentLikeFilter.builder().build()));

        // when
        commentService.toggleCommentLike(comment.getId(), user.getId());

        // then
        then(commentLikeFilterRepository).should().deleteByCommentIdAndUserId(comment.getId(), user.getId());
        then(commentLikeFilterRepository).should().decrementLikeCount(comment.getId());
    }

    @Test
    void generateAnonymousNickname_ShouldReturnUniqueNickname() {
        // given
        Long postId = post.getId();
        given(commentRepository.findByPostIdAndUserIdAndAnonymous(postId, user.getId(), true)).willReturn(Collections.emptyList());
        given(commentRepository.countByPostIdAndAnonymous(postId, true)).willReturn(0L);

        // when
        String nickname = commentService.generateAnonymousNickname(postId, user.getId());

        // then
        assertThat(nickname).isEqualTo("익명 1");
    }
}
