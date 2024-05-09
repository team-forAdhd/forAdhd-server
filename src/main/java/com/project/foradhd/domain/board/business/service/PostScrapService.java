package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.dto.PostScrapDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostScrapService {
    // 사용자의 스크랩한 게시글 목록 조회
    Page<GeneralPostDto> getScrapsByUser(String userId, Pageable pageable, SortOption sortOption);

    // 스크랩 생성
    PostScrapDto createScrap(PostScrapDto postScrapDto);

    // 스크랩 삭제
    void deleteScrap(String scrapId);
}