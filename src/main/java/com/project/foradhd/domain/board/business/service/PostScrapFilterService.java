package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.PostScrapFilterDto;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostScrapFilterService {
    // 사용자의 스크랩한 게시글 목록 조회
    Page<GeneralPostDto> getScrapsByUser(Long userId, Pageable pageable, SortOption sortOption);

    // 스크랩 생성
    PostScrapFilterDto createScrap(PostScrapFilterDto postScrapDto);

    // 스크랩 삭제
    void deleteScrap(Long scrapId);
}
