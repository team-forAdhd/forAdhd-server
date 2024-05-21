package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostLikeFilterService;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostLikeFilter;
import com.project.foradhd.domain.board.persistence.repository.PostLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.global.exception.UserNotFoundException;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BoardNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeFilterServiceImpl implements PostLikeFilterService {

    private final PostLikeFilterRepository postLikeFilterRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public void toggleLike(String userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postId + " not found"));

        User user = userService.getUser(userId);

        if (postLikeFilterRepository.existsByUserIdAndPostId(userId, postId)) {
            postLikeFilterRepository.deleteByUserIdAndPostId(userId, postId);
            post.decrementLikeCount();
        } else {
            PostLikeFilter newLike = PostLikeFilter.builder()
                    .user(user)
                    .post(post)
                    .build();
            postLikeFilterRepository.save(newLike);
            post.incrementLikeCount();
        }
        postRepository.save(post);
    }
}
