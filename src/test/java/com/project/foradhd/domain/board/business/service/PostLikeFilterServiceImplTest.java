package com.project.foradhd.domain.board.business.service;

import static org.mockito.BDDMockito.*;

import com.project.foradhd.domain.board.business.service.Impl.PostLikeFilterServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostLikeFilter;
import com.project.foradhd.domain.board.persistence.repository.PostLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class PostLikeFilterServiceImplTest {

    @Mock
    private PostLikeFilterRepository postLikeFilterRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostLikeFilterServiceImpl postLikeFilterService;

    @Test
    void shouldAddLikeWhenNotAlreadyLiked() {
        String userId = "user1";
        Long postId = 1L;
        Post post = mock(Post.class);
        User user = mock(User.class);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userService.getUser(userId)).willReturn(user);
        given(postLikeFilterRepository.existsByUserIdAndPostId(userId, postId)).willReturn(false);

        postLikeFilterService.toggleLike(userId, postId);

        then(postLikeFilterRepository).should().save(any(PostLikeFilter.class));
        then(post).should().incrementLikeCount();
        then(postRepository).should().save(post);
    }

    @Test
    void shouldRemoveLikeWhenAlreadyLiked() {
        String userId = "user1";
        Long postId = 1L;
        Post post = mock(Post.class);
        User user = mock(User.class);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userService.getUser(userId)).willReturn(user);
        given(postLikeFilterRepository.existsByUserIdAndPostId(userId, postId)).willReturn(true);

        postLikeFilterService.toggleLike(userId, postId);

        then(postLikeFilterRepository).should().deleteByUserIdAndPostId(userId, postId);
        then(post).should().decrementLikeCount();
        then(postRepository).should().save(post);
    }


}
