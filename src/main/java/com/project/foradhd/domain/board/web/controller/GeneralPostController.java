package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.business.service.GeneralPostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/general")
public class GeneralPostController {

    private final GeneralPostService postService;

    @Autowired
    public GeneralPostController(GeneralPostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<GeneralPostDto> getPost(@PathVariable Long postId) {
        GeneralPostDto postDto = postService.getPost(postId);
        return ResponseEntity.ok(postDto);
    }

    @PostMapping("/posts")
    public ResponseEntity<GeneralPostDto> createPost(@RequestBody GeneralPostDto postDto) {
        GeneralPostDto createdPost = postService.createPost(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<GeneralPostDto> updatePost(@PathVariable Long postId, @RequestBody GeneralPostDto postDto) {
        postDto.setPostId(postId);
        GeneralPostDto updatedPost = postService.updatePost(postDto);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<GeneralPostDto>> getAllPosts(Pageable pageable) {
        Page<GeneralPostDto> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/my-scraps")
    public ResponseEntity<Page<GeneralPostDto>> getMyScraps(@RequestParam Long userId, Pageable pageable) {
        Page<GeneralPostDto> myScraps = postService.getMyScraps(userId, pageable);
        return ResponseEntity.ok(myScraps);
    }

    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<Page<GeneralPostDto>> getUserPosts(@PathVariable Long userId, Pageable pageable, @RequestParam SortOption sortOption) {
        Page<GeneralPostDto> userPosts = postService.getUserPosts(userId, pageable, sortOption);
        return ResponseEntity.ok(userPosts);
    }

    @GetMapping("/posts/category/{categoryId}")
    public ResponseEntity<Page<GeneralPostDto>> listPostsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        Page<GeneralPostDto> postsByCategory = postService.listPosts(categoryId, pageable);
        return ResponseEntity.ok(postsByCategory);
    }
}
