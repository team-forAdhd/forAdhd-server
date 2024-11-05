package com.project.foradhd.domain.board.business.service;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.foradhd.domain.board.business.service.Impl.PostServiceImpl;
import com.project.foradhd.domain.board.fixtures.PostFixtures;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.Category;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.user.fixtures.UserFixtures;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.util.SseEmitters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import java.util.List;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    PostRepository postRepository;

    @Mock
    NotificationService notificationService;

    @Mock
    SseEmitters sseEmitters;

    @Mock
    PostSearchHistoryService searchHistoryService;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    void shouldGetPostById() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        //when
        Post foundPost = postService.getPost(post.getId());

        //then
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getId()).isEqualTo(post.getId());
        assertThat(foundPost.getTitle()).isEqualTo("테스트 게시글");

        then(postRepository).should(times(1)).findById(post.getId());
    }

    @Test
    void shouldCreatePost() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        given(postRepository.save(post)).willReturn(post);

        //when
        Post createdPost = postService.createPost(post);

        //then
        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getTitle()).isEqualTo("테스트 게시글");
        then(postRepository).should(times(1)).save(post);
    }

    @Test
    void shouldUpdatePost() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Post updatedPost = post.toBuilder().title("수정된 제목").build();

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(updatedPost);

        //when
        Post result = postService.updatePost(updatedPost);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("수정된 제목");

        then(postRepository).should(times(1)).findById(post.getId());
        then(postRepository).should(times(1)).save(any(Post.class));
    }

    @Test
    void shouldDeletePost() {
        //given
        Long postId = 1L;

        //when
        postService.deletePost(postId);

        //then
        then(postRepository).should(times(1)).deleteById(postId);
    }

    @Test
    void shouldGetAllPosts() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findAll(pageable)).willReturn(postPage);

        //when
        Page<Post> result = postService.getAllPosts(pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("테스트 게시글");

        then(postRepository).should(times(1)).findAll(pageable);
    }

    @Test
    void shouldGetUserPosts() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findByUserIdWithUserProfile(eq(user.getId()), any(Pageable.class))).willReturn(postPage);

        //when
        Page<Post> result = postService.getUserPosts(user.getId(), pageable, SortOption.NEWEST_FIRST);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUser().getId()).isEqualTo(user.getId());

        then(postRepository).should(times(1)).findByUserIdWithUserProfile(eq(user.getId()), any(Pageable.class));
    }

    @Test
    void shouldGetUserPostsByCategory() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).category(Category.TEENS).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findByUserIdAndCategoryWithUserProfile(eq(user.getId()), eq(Category.TEENS), any(Pageable.class))).willReturn(postPage);

        //when
        Page<Post> result = postService.getUserPostsByCategory(user.getId(), Category.TEENS, pageable, SortOption.NEWEST_FIRST);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory()).isEqualTo(Category.TEENS);

        then(postRepository).should(times(1)).findByUserIdAndCategoryWithUserProfile(eq(user.getId()), eq(Category.TEENS), any(Pageable.class));
    }

    @Test
    void shouldListPostsByCategory() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).category(Category.TEENS).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findByCategoryWithUserProfile(Category.TEENS, pageable)).willReturn(postPage);

        //when
        Page<Post> result = postService.listByCategory(Category.TEENS, pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory()).isEqualTo(Category.TEENS);

        then(postRepository).should(times(1)).findByCategoryWithUserProfile(Category.TEENS, pageable);
    }

    @Test
    void shouldGetAndIncrementViewCount() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        //when
        Post result = postService.getAndIncrementViewCount(post.getId());

        //then
        assertThat(result).isNotNull();
        assertThat(result.getViewCount()).isEqualTo(1);

        then(postRepository).should(times(1)).findById(post.getId());
    }

    @Test
    void shouldGetTopPosts() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findTopPostsWithUserProfile(pageable)).willReturn(postPage);

        //when
        Page<Post> result = postService.getTopPosts(pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        then(postRepository).should(times(1)).findTopPostsWithUserProfile(pageable);
        then(notificationService).should(times(1)).createNotification(user.getId(), "내 글이 TOP 10 게시물로 선정됐어요!");
        then(sseEmitters).should(times(1)).sendNotification(user.getId(), "내 글이 TOP 10 게시물로 선정됐어요!");
    }

    @Test
    void shouldGetTopPostsByCategory() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).category(Category.TEENS).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        given(postRepository.findTopPostsByCategoryWithUserProfile(Category.TEENS, pageable)).willReturn(postPage);

        //when
        Page<Post> result = postService.getTopPostsByCategory(Category.TEENS, pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        then(postRepository).should(times(1)).findTopPostsByCategoryWithUserProfile(Category.TEENS, pageable);
        then(notificationService).should(times(1)).createNotification(user.getId(), "내 글이 TOP 10 게시물로 선정됐어요!");
        then(sseEmitters).should(times(1)).sendNotification(user.getId(), "내 글이 TOP 10 게시물로 선정됐어요!");
    }

    @Test
    void shouldAddComment() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        String commentContent = "This is a comment";

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        //when
        postService.addComment(post.getId(), commentContent, user.getId());

        //then
        then(postRepository).should(times(1)).findById(post.getId());
        then(notificationService).should(times(1)).createNotification(user.getId(), "새로운 댓글이 달렸어요: " + commentContent);
        then(sseEmitters).should(times(1)).sendNotification(user.getId(), "새로운 댓글이 달렸어요: " + commentContent);
    }

    @Test
    void shouldGetRecentSearchTerms() {
        //given
        String userId = "userId";
        List<String> searchTerms = Arrays.asList("term1", "term2");

        given(searchHistoryService.getRecentSearchTerms(userId)).willReturn(searchTerms);

        //when
        List<String> result = postService.getRecentSearchTerms(userId);

        //then
        assertThat(result).isEqualTo(searchTerms);

        then(searchHistoryService).should(times(1)).getRecentSearchTerms(userId);
    }

    @Test
    void shouldSearchPostsByTitle() {
        //given
        User user = UserFixtures.toUser().build();
        Post post = PostFixtures.toPost(user).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Arrays.asList(post));

        String title = "test title";
        String userId = user.getId();

        given(postRepository.findByTitleContainingWithUserProfile(title, pageable)).willReturn(postPage);

        //when
        Page<Post> result = postService.searchPostsByTitle(title, userId, pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        then(postRepository).should(times(1)).findByTitleContainingWithUserProfile(title, pageable);
        then(searchHistoryService).should(times(1)).saveSearchTerm(userId, title);
    }
}
