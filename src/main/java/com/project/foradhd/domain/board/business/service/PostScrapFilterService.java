package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostScrapFilterService {
    void toggleScrap(Long postId, String userId);
    long getCommentCount(Long postId);
    Page<PostScrapFilter> getScrapsByUserAndCategory(String userId, CategoryName category, Pageable pageable, SortOption sortOption);
}