package com.project.foradhd.domain.board.business.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import com.project.foradhd.domain.board.business.service.Impl.PostServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapFilterRepository;
import com.project.foradhd.domain.board.web.dto.response.PostRankingResponseDto;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.util.SseEmitters;
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

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private SseEmitters sseEmitters;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id("user123").build();
        post = Post.builder()
                .id(1L)
                .user(user)
                .title("Test Post")
                .content("This is a test post")
                .anonymous(false)
                .likeCount(0)
                .commentCount(0)
                .scrapCount(0)
                .viewCount(0)
                .build();
    }

    @Test
    void getPost_ShouldReturnPost() {
        // given
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        // when
        Post result = postService.getPost(post.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Post");
    }

    @Test
    void createPost_ShouldSavePost() {
        // given
        given(postRepository.save(post)).willReturn(post);

        // when
        Post result = postService.createPost(post);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Post");
    }

    @Test
    void updatePost_ShouldUpdatePost() {
        // given
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        Post updatedPost = post.toBuilder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        given(postRepository.save(any(Post.class))).willReturn(updatedPost);

        // when
        Post result = postService.updatePost(updatedPost);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getContent()).isEqualTo("Updated Content");
    }

    @Test
    void deletePost_ShouldDeletePost() {
        // given
        willDoNothing().given(postRepository).deleteById(post.getId());

        // when
        postService.deletePost(post.getId());

        // then
        then(postRepository).should().deleteById(post.getId());
    }

    @Test
    void getAllPosts_ShouldReturnPagedPosts() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Collections.singletonList(post), pageable, 1);
        given(postRepository.findAll(pageable)).willReturn(postPage);

        // when
        Page<Post> result = postService.getAllPosts(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void getUserPosts_ShouldReturnPagedUserPosts() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = new PageImpl<>(Collections.singletonList(post), pageable, 1);
        given(postRepository.findByUserId(user.getId(), pageable)).willReturn(postPage);

        // when
        Page<Post> result = postService.getUserPosts(user.getId(), pageable, SortOption.NEWEST_FIRST);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test Post");
    }

    @Test
    void listByCategory_ShouldReturnPagedPostsByCategory() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Collections.singletonList(post), pageable, 1);
        given(postRepository.findByCategory(post.getCategory(), pageable)).willReturn(postPage);

        // when
        Page<Post> result = postService.listByCategory(post.getCategory(), pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void getAndIncrementViewCount_ShouldIncrementViewCount() {
        // given
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(post.toBuilder().viewCount(1).build());

        // when
        Post result = postService.getAndIncrementViewCount(post.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getViewCount()).isEqualTo(1);
    }

    @Test
    void getTopPosts_ShouldReturnTopPosts() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> topPosts = Collections.singletonList(post);
        given(postRepository.findTopPosts(pageable)).willReturn(topPosts);

        // when
        List<PostRankingResponseDto> result = postService.getTopPosts(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Post");
    }

    @Test
    void getTopPostsByCategory_ShouldReturnTopPostsByCategory() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> topPosts = Collections.singletonList(post);
        given(postRepository.findTopPostsByCategory(post.getCategory(), pageable)).willReturn(topPosts);

        // when
        List<PostRankingResponseDto> result = postService.getTopPostsByCategory(post.getCategory(), pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Post");
    }

    @Test
    void addComment_ShouldNotifyUser() {
        // given
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        // when
        postService.addComment(post.getId(), "New comment", user.getId());

        // then
        then(notificationService).should().createNotification(eq(post.getUser().getId()), anyString());
        then(sseEmitters).should().sendNotification(eq(post.getUser().getId()), anyString());
    }
}
