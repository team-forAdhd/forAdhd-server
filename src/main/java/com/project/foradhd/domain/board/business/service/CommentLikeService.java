package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.entity.CommentLike;
import com.project.foradhd.domain.board.persistence.repository.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface CommentLikeService {
    void toggleLike(String userId, String commentId);
}
