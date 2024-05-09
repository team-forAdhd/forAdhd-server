package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.GeneralBoardService;
import com.project.foradhd.domain.board.business.service.PostLikeService;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.global.exception.BoardNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    // 특정 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<GeneralPostDto> getPost(@PathVariable String postId) {
        try {
            GeneralPostDto post = service.getPost(postId);
            return ResponseEntity.ok(post);
        } catch (BoardNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 새 게시글 생성
    @PostMapping
    public ResponseEntity<GeneralPostDto> createPost(@RequestBody GeneralPostDto postDTO) {
        GeneralPostDto createdPost = service.createPost(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String postId) {
        try {
            service.deletePost(postId);
            return ResponseEntity.ok().build();
        } catch (BoardNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글 업데이트
    @PutMapping("/{postId}")
    public ResponseEntity<GeneralPostDto> updatePost(@PathVariable String postId, @RequestBody GeneralPostDto postDTO) {
        try {
            postDTO.setPostId(postId);
            GeneralPostDto updatedPost = service.updatePost(postDTO);
            return ResponseEntity.ok(updatedPost);
        } catch (BoardNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 전체 게시글 목록 조회
    @GetMapping("/all")
    public ResponseEntity<Page<GeneralPostDto>> getAllPosts(Pageable pageable) {
        Page<GeneralPostDto> posts = service.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // 사용자의 게시글 목록 조회
    @GetMapping("/user/{writerId}")
    public ResponseEntity<Page<GeneralPostDto>> getMyPosts(@PathVariable String writerId, Pageable pageable) {
        Page<GeneralPostDto> myPosts = service.getMyPosts(writerId, pageable);
        return ResponseEntity.ok(myPosts);
    }


    // 사용자의 스크랩 목록 조회
    @GetMapping("/scraps/user/{userId}")
    public ResponseEntity<Page<GeneralPostDto>> getMyScraps(@PathVariable String userId, Pageable pageable) {
        Page<GeneralPostDto> scraps = service.getMyScraps(userId, pageable);
        return ResponseEntity.ok(scraps);
    }

    // 카테고리별 게시글 목록 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<GeneralPostDto>> getPostsByCategory(@PathVariable String categoryId, Pageable pageable) {
        try {
            Page<GeneralPostDto> posts = service.getPostsByCategory(categoryId, pageable);
            return ResponseEntity.ok(posts);
        } catch (BoardNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글에 대한 좋아요 토글
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
