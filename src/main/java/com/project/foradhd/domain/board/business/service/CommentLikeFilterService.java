package com.project.foradhd.domain.board.business.service;

public interface CommentLikeFilterService {
    void toggleLike(Long userId, Long commentId);
}
