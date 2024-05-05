package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.ScrapService;
import com.project.foradhd.domain.board.persistence.enums.PostSortOption;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{userId}/scrap")
public class ScrapController {
    private final ScrapService scrapService;

    @Autowired
    public ScrapController(ScrapService scrapService) {
        this.scrapService = scrapService;
    }

    @GetMapping
    public ResponseEntity<List<GeneralPostDto>> getScraps(@RequestParam String userId, @RequestParam(defaultValue = "NEWEST") PostSortOption sortOption) {
        List<GeneralPostDto> scraps = scrapService.getScraps(userId, sortOption);
        return ResponseEntity.ok(scraps);
    }
}
