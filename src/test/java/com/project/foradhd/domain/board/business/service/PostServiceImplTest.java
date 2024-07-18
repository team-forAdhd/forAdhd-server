package com.project.foradhd.domain.board.business.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import com.project.foradhd.domain.board.business.service.Impl.PostServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapFilterRepository;
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

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostScrapFilterRepository scrapFilterRepository;

    @Mock
    private PostScrapFilterService postScrapFilterService;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void shouldFetchPostById() {
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        Post foundPost = postService.getPost(postId);

        then(postRepository).should().findById(postId);
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getId()).isEqualTo(postId);
    }

    @Test
    void shouldSaveNewPost() {
        Post post = new Post();
        post.setTitle("New Post");
        given(postRepository.save(any(Post.class))).willReturn(post);

        Post savedPost = postService.createPost(post);

        then(postRepository).should().save(post);
        assertThat(savedPost.getTitle()).isEqualTo("New Post");
    }

    @Test
    void shouldUpdatePost() {
        Long postId = 1L;
        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setTitle("Old Title");
        Post updatedDetails = new Post();
        updatedDetails.setId(postId);
        updatedDetails.setTitle("Updated Title");
        given(postRepository.findById(postId)).willReturn(Optional.of(existingPost));
        given(postRepository.save(any(Post.class))).willReturn(updatedDetails);

        Post updatedPost = postService.updatePost(updatedDetails);

        then(postRepository).should().findById(postId);
        then(postRepository).should().save(any(Post.class));
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void shouldDeletePost() {
        Long postId = 1L;
        willDoNothing().given(postRepository).deleteById(postId);

        postService.deletePost(postId);

        then(postRepository).should().deleteById(postId);
    }

    @Test
    void shouldRetrieveAllPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        given(postRepository.findAll(pageable)).willReturn(new PageImpl<>(Collections.emptyList()));

        Page<Post> posts = postService.getAllPosts(pageable);

        then(postRepository).should().findAll(pageable);
        assertThat(posts).isEmpty();
    }
}
