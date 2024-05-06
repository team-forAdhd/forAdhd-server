package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.dto.PostScrapDto;

import java.util.List;

public interface PostScrapService {
    // 사용자의 스크랩한 게시글 목록 조회
    List<GeneralPostDto> getScrapsByUser(String userId);

    // 스크랩 생성
    PostScrapDto createScrap(PostScrapDto postScrapDto);

    // 스크랩 ID로 스크랩 조회
    PostScrapDto getScrapById(String scrapId);

    // 스크랩 삭제
    void deleteScrap(String scrapId);
}