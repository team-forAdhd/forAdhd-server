package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.PostScrapService;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.dto.PostScrapDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/scrap/")
public class PostScrapController {

    private final PostScrapService postScrapService;

    @Autowired
    public PostScrapController(PostScrapService postScrapService) {
        this.postScrapService = postScrapService;
    }

    // 내가 스크랩한 게시글 조회
    @GetMapping("/{userId}")
    public ResponseEntity<Page<GeneralPostDto>> getScrapsByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "NEWEST_FIRST") SortOption sortOption,
            Pageable pageable) {

        Page<GeneralPostDto> scraps = postScrapService.getScrapsByUser(userId, pageable, sortOption);
        return ResponseEntity.ok(scraps);
    }

    // 스크랩 생성
    @PostMapping
    public ResponseEntity<PostScrapDto> createScrap(@RequestBody PostScrapDto postScrapDto) {
        PostScrapDto createdScrap = postScrapService.createScrap(postScrapDto);
        return new ResponseEntity<>(createdScrap, HttpStatus.CREATED);
    }


    // 스크랩 삭제
    @DeleteMapping("/{scrapId}")
    public ResponseEntity<Void> deleteScrap(@PathVariable String scrapId) {
        postScrapService.deleteScrap(scrapId);
        return ResponseEntity.noContent().build();
    }
}
