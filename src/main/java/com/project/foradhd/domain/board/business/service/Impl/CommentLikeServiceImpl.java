package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.CommentLikeService;
import com.project.foradhd.domain.board.persistence.entity.CommentLike;
import com.project.foradhd.domain.board.persistence.repository.CommentLikeRepository;
import com.project.foradhd.domain.board.persistence.repository.GeneralCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final GeneralCommentRepository commentRepository;

    @Autowired
    public CommentLikeServiceImpl(CommentLikeRepository commentLikeRepository, GeneralCommentRepository commentRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void toggleLike(String userId, String commentId) {
        Optional<CommentLike> existingLike = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);

        if (existingLike.isPresent()) {
            commentLikeRepository.deleteByUserIdAndCommentId(userId, commentId);
            commentRepository.decrementLikeCount(commentId);
        } else {
            CommentLike newLike = CommentLike.builder()
                    .commentLikeFilterId(UUID.randomUUID().toString())
                    .userId(userId)
                    .commentId(commentId)
                    .createdAt(LocalDateTime.now())
                    .build();
            commentLikeRepository.save(newLike);
            commentRepository.incrementLikeCount(commentId);
        }
    }
}
