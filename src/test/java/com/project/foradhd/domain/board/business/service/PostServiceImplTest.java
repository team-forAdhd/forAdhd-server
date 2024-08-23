package com.project.foradhd.domain.board.business.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import com.project.foradhd.domain.board.business.service.Impl.PostServiceImpl;
import com.project.foradhd.domain.board.fixtures.PostFixtures;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapFilterRepository;
import com.project.foradhd.domain.board.web.dto.response.PostRankingResponseDto;
import com.project.foradhd.domain.user.fixtures.UserFixtures;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.util.SseEmitters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
@MockitoSettings(strictness = Strictness.LENIENT)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private SseEmitters sseEmitters;

    @Mock
    private PostSearchHistoryService searchHistoryService;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void shouldGetPostById() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        Post foundPost = postService.getPost(post.getId());

        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getId()).isEqualTo(post.getId());
        assertThat(foundPost.getTitle()).isEqualTo("테스트 게시글");

        then(postRepository).should(times(1)).findById(post.getId());
    }

    @Test
    public void shouldCreatePost() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        given(postRepository.save(post)).willReturn(post);

        Post createdPost = postService.createPost(post);

        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getTitle()).isEqualTo("테스트 게시글");

        then(postRepository).should(times(1)).save(post);
    }

    @Test
    public void shouldUpdatePost() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Post updatedPost = post.toBuilder().title("수정된 제목").build();

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(postRepository.save(updatedPost)).willReturn(updatedPost);

        Post result = postService.updatePost(updatedPost);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("수정된 제목");

        then(postRepository).should(times(1)).findById(post.getId());
        then(postRepository).should(times(1)).save(updatedPost);
    }

    @Test
    public void shouldDeletePost() {
        Long postId = 1L;

        postService.deletePost(postId);

        then(postRepository).should(times(1)).deleteById(postId);
    }

    @Test
    public void shouldGetAllPosts() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findAll(pageable)).willReturn(postPage);

        Page<Post> result = postService.getAllPosts(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("테스트 게시글");

        then(postRepository).should(times(1)).findAll(pageable);
    }

    @Test
    public void shouldGetUserPosts() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findByUserIdWithUserProfile(user.getId(), pageable)).willReturn(postPage);

        Page<Post> result = postService.getUserPosts(user.getId(), pageable, SortOption.NEWEST_FIRST);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUser().getId()).isEqualTo(user.getId());

        then(postRepository).should(times(1)).findByUserIdWithUserProfile(user.getId(), pageable);
    }

    @Test
    public void shouldGetUserPostsByCategory() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).category(CategoryName.TEENS).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findByUserIdAndCategoryWithUserProfile(user.getId(), CategoryName.TEENS, pageable)).willReturn(postPage);

        Page<Post> result = postService.getUserPostsByCategory(user.getId(), CategoryName.TEENS, pageable, SortOption.NEWEST_FIRST);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory()).isEqualTo(CategoryName.TEENS);

        then(postRepository).should(times(1)).findByUserIdAndCategoryWithUserProfile(user.getId(), CategoryName.TEENS, pageable);
    }

    @Test
    public void shouldListPostsByCategory() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).category(CategoryName.TEENS).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findByCategoryWithUserProfile(CategoryName.TEENS, pageable)).willReturn(postPage);

        Page<Post> result = postService.listByCategory(CategoryName.TEENS, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory()).isEqualTo(CategoryName.TEENS);

        then(postRepository).should(times(1)).findByCategoryWithUserProfile(CategoryName.TEENS, pageable);
    }

    @Test
    public void shouldGetAndIncrementViewCount() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        Post result = postService.getAndIncrementViewCount(post.getId());

        assertThat(result).isNotNull();
        assertThat(result.getViewCount()).isEqualTo(1);

        then(postRepository).should(times(1)).findById(post.getId());
    }

    @Test
    public void shouldGetTopPosts() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findTopPostsWithUserProfile(pageable)).willReturn(postPage);

        Page<Post> result = postService.getTopPosts(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        then(postRepository).should(times(1)).findTopPostsWithUserProfile(pageable);
        then(notificationService).should(times(1)).createNotification(user.getId(), "내 글이 TOP 10 게시물로 선정됐어요!");
        then(sseEmitters).should(times(1)).sendNotification(user.getId(), "내 글이 TOP 10 게시물로 선정됐어요!");
    }

    @Test
    public void shouldGetTopPostsByCategory() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).category(CategoryName.TEENS).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findTopPostsByCategoryWithUserProfile(CategoryName.TEENS, pageable)).willReturn(postPage);

        Page<Post> result = postService.getTopPostsByCategory(CategoryName.TEENS, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        then(postRepository).should(times(1)).findTopPostsByCategoryWithUserProfile(CategoryName.TEENS, pageable);
        then(notificationService).should(times(1)).createNotification(user.getId(), "내 글이 TOP 10 게시물로 선정됐어요!");
        then(sseEmitters).should(times(1)).sendNotification(user.getId(), "내 글이 TOP 10 게시물로 선정됐어요!");
    }

    @Test
    public void shouldAddComment() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        String commentContent = "This is a comment";

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        postService.addComment(post.getId(), commentContent, user.getId());

        then(postRepository).should(times(1)).findById(post.getId());
        then(notificationService).should(times(1)).createNotification(user.getId(), "새로운 댓글이 달렸어요: " + commentContent);
        then(sseEmitters).should(times(1)).sendNotification(user.getId(), "새로운 댓글이 달렸어요: " + commentContent);
    }

    @Test
    public void shouldGetRecentSearchTerms() {
        String userId = "user-id";
        List<String> searchTerms = Arrays.asList("term1", "term2");

        given(searchHistoryService.getRecentSearchTerms(userId)).willReturn(searchTerms);

        List<String> result = postService.getRecentSearchTerms(userId);

        assertThat(result).isEqualTo(searchTerms);

        then(searchHistoryService).should(times(1)).getRecentSearchTerms(userId);
    }

    @Test
    public void shouldSearchPostsByTitle() {
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        String title = "test title";
        String userId = user.getId();

        given(postRepository.findByTitleContainingWithUserProfile(title, pageable)).willReturn(postPage);

        Page<Post> result = postService.searchPostsByTitle(title, userId, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        then(postRepository).should(times(1)).findByTitleContainingWithUserProfile(title, pageable);
        then(searchHistoryService).should(times(1)).saveSearchTerm(userId, title);
    }
}
