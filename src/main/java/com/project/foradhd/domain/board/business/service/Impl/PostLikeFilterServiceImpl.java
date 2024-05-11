package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostLikeFilterService;
import com.project.foradhd.domain.board.persistence.entity.PostLikeFilter;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.repository.PostLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.GeneralPostRepository;
import com.project.foradhd.global.exception.UserNotFoundException;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.global.exception.BoardNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Service
@Transactional
public class PostLikeFilterServiceImpl implements PostLikeFilterService {

    private final PostLikeFilterRepository postLikeFilterRepository;
    private final GeneralPostRepository generalPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostLikeFilterServiceImpl(PostLikeFilterRepository postLikeFilterRepository, GeneralPostRepository generalPostRepository, UserRepository userRepository) {
        this.postLikeFilterRepository = postLikeFilterRepository;
        this.generalPostRepository = generalPostRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void toggleLike(Long userId, Long postId) {
        GeneralPost post = generalPostRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postId + " not found"));

        User user = userRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        if (postLikeFilterRepository.existsByUserIdAndPostId(userId, postId)) {
            postLikeFilterRepository.deleteByUserIdAndPostId(userId, postId);
            post.decrementLikeCount();
        } else {
            PostLikeFilter newLike = new PostLikeFilter();
            newLike.setUser(user);
            newLike.setPost(post);
            newLike.setCreatedAt(LocalDateTime.now());
            postLikeFilterRepository.save(newLike);
            post.incrementLikeCount();
        }
        generalPostRepository.save(post);
    }

}
