package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostLikeService;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.entity.PostLike;
import com.project.foradhd.domain.board.persistence.repository.GeneralBoardRepository;
import com.project.foradhd.domain.board.persistence.repository.PostLikeRepository;
import com.project.foradhd.global.exception.BoardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final GeneralBoardRepository generalBoardRepository;

    @Autowired
    public PostLikeServiceImpl(PostLikeRepository postLikeRepository, GeneralBoardRepository generalBoardRepository) {
        this.postLikeRepository = postLikeRepository;
        this.generalBoardRepository = generalBoardRepository;
    }

    @Override
    public void toggleLike(String userId, String postId) {
        GeneralPost post = generalBoardRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postId + " not found"));

        if (postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
            postLikeRepository.deleteByUserIdAndPostId(userId, postId);
            post.decrementLikeCount();
        } else {
            PostLike newLike = new PostLike();
            newLike.setUserId(userId);
            newLike.setPostId(postId);
            newLike.setCreatedAt(LocalDateTime.now());
            postLikeRepository.save(newLike);
            post.incrementLikeCount();
        }
        generalBoardRepository.save(post);
    }
}
