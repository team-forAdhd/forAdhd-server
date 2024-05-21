package com.project.foradhd.domain.board.business.service;

public interface PostLikeFilterService {
    void toggleLike(String userId, Long postId);
}
