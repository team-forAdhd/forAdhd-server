package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostLikeService;
import com.project.foradhd.domain.board.persistence.entity.PostLike;
import com.project.foradhd.domain.board.persistence.repository.GeneralBoardRepository;
import com.project.foradhd.domain.board.persistence.repository.PostLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private GeneralBoardRepository generalBoardRepository;

    @Override
    @Transactional
    public void toggleLike(String userId, String postId) {
        if (postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
            postLikeRepository.deleteByUserIdAndPostId(userId, postId);
            generalBoardRepository.decrementLikeCount(postId); // 좋아요 수 감소
        } else {
            PostLike postLike = PostLike.builder()
                    .userId(userId)
                    .postId(postId)
                    .createdAt(LocalDateTime.now())
                    .build();
            postLikeRepository.save(postLike);
            generalBoardRepository.incrementLikeCount(postId); // 좋아요 수 증가
        }
    }
}