package com.project.foradhd.domain.board.business.service;

import static org.mockito.BDDMockito.*;

import com.project.foradhd.domain.board.business.service.Impl.PostScrapFilterServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapFilterRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostScrapFilterServiceImplTest {

    @Mock
    PostScrapFilterRepository scrapFilterRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    UserService userService;

    @InjectMocks
    PostScrapFilterServiceImpl postScrapFilterService;

    @Test
    void shouldAddNewScrapWhenNoneExists() {
        //given
        Long postId = 1L;
        String userId = "user1";
        Post post = mock(Post.class); // Post 객체를 mock 객체로 변경
        User user = mock(User.class); // User 객체도 mock 객체로 변경, 필요에 따라

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userService.getUser(userId)).willReturn(user);
        given(scrapFilterRepository.findByPostIdAndUserId(postId, userId)).willReturn(Optional.empty());

        //when
        postScrapFilterService.toggleScrap(postId, userId);

        //then
        then(scrapFilterRepository).should().save(any(PostScrapFilter.class));
        then(post).should().incrementScrapCount(); // 이제 verify 사용 가능
    }

    @Test
    void shouldRemoveScrapWhenExists() {
        //given
        Long postId = 1L;
        String userId = "user1";
        Post post = mock(Post.class);
        User user = mock(User.class);
        PostScrapFilter existingScrap = new PostScrapFilter();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userService.getUser(userId)).willReturn(user);
        given(scrapFilterRepository.findByPostIdAndUserId(postId, userId)).willReturn(Optional.of(existingScrap));

        //when
        postScrapFilterService.toggleScrap(postId, userId);

        //then
        then(scrapFilterRepository).should().delete(existingScrap);
        then(post).should().decrementScrapCount(); // 이제 verify 사용 가능
    }
}
