package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.CommentLikeFilterService;
import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.CommentLikeFilter;
import com.project.foradhd.domain.board.persistence.repository.CommentLikeFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CommentLikeFilterServiceImpl implements CommentLikeFilterService {

    private final CommentLikeFilterRepository commentLikeFilterRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentLikeFilterServiceImpl(CommentLikeFilterRepository commentLikeFilterRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.commentLikeFilterRepository = commentLikeFilterRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void toggleLike(Long userId, Long commentId) {
        User user = userRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("No user found with ID: " + userId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("No comment found with ID: " + commentId));

        if (commentLikeFilterRepository.existsByUserIdAndCommentId(userId, commentId)) {
            commentLikeFilterRepository.deleteByUserIdAndCommentId(userId, commentId);
            commentRepository.decrementLikeCount(commentId);
        } else {
            CommentLikeFilter newLike = CommentLikeFilter.builder()
                    .user(user)
                    .comment(comment)
                    .createdAt(LocalDateTime.now())
                    .build();
            commentLikeFilterRepository.save(newLike);
            commentRepository.incrementLikeCount(commentId);
        }
    }
}
