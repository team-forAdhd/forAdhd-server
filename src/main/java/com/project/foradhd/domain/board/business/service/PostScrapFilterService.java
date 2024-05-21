package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostScrapFilterService {
    void toggleScrap(Long postId, String userId);
    Page<PostScrapFilter> getScrapsByUser(String userId, Pageable pageable, SortOption sortOption);
}