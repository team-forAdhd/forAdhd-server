package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.GeneralBoardService;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/posts")
public class GeneralBoardController {
    private final GeneralBoardService service;

    public GeneralBoardController(GeneralBoardService service) {
        this.service = service;
    }

    @GetMapping("/{postId}")
    public GeneralPostDto getPost(@PathVariable String postId) {
        return service.getPost(postId);
    }

    @PostMapping
    public GeneralPostDto createPost(@RequestBody GeneralPostDto postDTO) {
        return service.createPost(postDTO);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable String postId) {
        service.deletePost(postId);
    }

    @PutMapping
    public GeneralPostDto updatePost(@RequestBody GeneralPostDto postDTO) {
        return service.updatePost(postDTO);
    }

    // 게시글 목록 조회, 카테고리별 필터링
    @GetMapping
    public ResponseEntity<List<GeneralPostDto>> listPosts(@RequestParam(required = false) String category) {
        List<GeneralPostDto> posts = service.listPosts(category);
        return ResponseEntity.ok(posts);
    }
}
