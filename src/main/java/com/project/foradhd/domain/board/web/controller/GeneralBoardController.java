package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.GeneralBoardService;
import com.project.foradhd.domain.board.business.service.PostLikeService;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/posts")
public class GeneralBoardController {
    private final GeneralBoardService service;
    private final PostLikeService postLikeService;

    public GeneralBoardController(GeneralBoardService service, PostLikeService postLikeService) {
        this.service = service;
        this.postLikeService = postLikeService;
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

    @PostMapping("/{postId}/likes")
    public ResponseEntity<String> toggleLike(@PathVariable String postId, @RequestParam String userId) {
        try {
            postLikeService.toggleLike(userId, postId);
            return ResponseEntity.ok("Successfully toggled like for post " + postId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error toggling like: " + e.getMessage());
        }
    }

}
