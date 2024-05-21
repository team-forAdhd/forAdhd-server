package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.PostDto;
import com.project.foradhd.domain.board.web.dto.PostScrapFilterDto;
import com.project.foradhd.domain.board.business.service.PostScrapFilterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v1/posts/scrap/")
public class PostScrapFilterController {
    private final PostScrapFilterService postScrapFilterService;

    @Autowired
    public PostScrapFilterController(PostScrapFilterService postScrapFilterService) {
        this.postScrapFilterService = postScrapFilterService;
    }

    // 내가 스크랩한 게시글 조회
    @GetMapping("/{userId}")
    public ResponseEntity<Page<PostDto>> getScrapsByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "NEWEST_FIRST") SortOption sortOption,
            Pageable pageable) {

        Page<PostDto> scraps = postScrapFilterService.getScrapsByUser(userId, pageable, sortOption);
        return ResponseEntity.ok(scraps);
    }

    // 스크랩 생성
    @PostMapping
    public ResponseEntity<PostScrapFilterDto> createScrap(@RequestBody PostScrapFilterDto postScrapDto) {
        PostScrapFilterDto createdScrap = postScrapFilterService.createScrap(postScrapDto);
        return new ResponseEntity<>(createdScrap, HttpStatus.CREATED);
    }

    // 스크랩 삭제
    @DeleteMapping("/{scrapId}")
    public ResponseEntity<Void> deleteScrap(@PathVariable Long scrapId) {
        postScrapFilterService.deleteScrap(scrapId);
        return ResponseEntity.noContent().build();
    }


}
