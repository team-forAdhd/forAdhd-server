package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.PostScrapFilterService;
import com.project.foradhd.domain.board.business.service.PostService;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.PostDto;
import com.project.foradhd.domain.board.web.dto.PostScrapFilterDto;
import com.project.foradhd.domain.board.web.dto.request.PostRequestDto;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import com.project.foradhd.domain.board.web.mapper.PostMapper;
import com.project.foradhd.domain.board.web.mapper.PostScrapFilterMapper;
import com.project.foradhd.global.AuthUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final PostScrapFilterService postScrapFilterService;
    private final PostScrapFilterMapper postScrapFilterMapper;

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    @PostMapping()
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto) {
        Post post = postMapper.toEntity(postRequestDto);
        Post createdPost = postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.responsetoDto(createdPost));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        Post post = postMapper.toEntity(postRequestDto);
        post.setId(postId);
        Post updatedPost = postService.updatePost(post);
        return ResponseEntity.ok(postMapper.responsetoDto(updatedPost));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PostDto>> getAllPosts(Pageable pageable) {
        Page<Post> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts.map(postMapper::toDto));
    }

    // 메인 홈 - 카테고리 별 글 조회
    @GetMapping("/category")
    public ResponseEntity<Page<PostResponseDto>> getPostsByCategory(
            @RequestParam("category") CategoryName category,
            Pageable pageable) {

        Page<Post> posts = postService.listByCategory(category, pageable);
        Page<PostResponseDto> response = posts.map(postMapper::responsetoDto);  // Post to PostResponse 변환
        return ResponseEntity.ok(response);
    }

    // 마이페이지 - 나의 글
    @GetMapping("/{userId}/my-posts")
    public ResponseEntity<Page<PostDto>> getUserPosts(@AuthUserId String userId, Pageable pageable, @RequestParam SortOption sortOption) {
        Page<Post> userPosts = postService.getUserPosts(userId, pageable, sortOption);
        Page<PostDto> userPostsDto = userPosts.map(postMapper::toDto);
        return ResponseEntity.ok(userPostsDto);
    }

    @GetMapping("/scrap/{userId}")
    public ResponseEntity<Page<PostScrapFilterDto>> getScrapsByUser(@AuthUserId String userId, Pageable pageable, @RequestParam SortOption sortOption) {
        Page<PostScrapFilter> scraps = postScrapFilterService.getScrapsByUser(userId, pageable, sortOption);
        Page<PostScrapFilterDto> dtoPage = scraps.map(postScrapFilterMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    @PutMapping("/scrap/{postId}/")
    public ResponseEntity<Void> toggleScrap(@PathVariable Long postId, @AuthUserId String userId) {
        postScrapFilterService.toggleScrap(postId, userId);
        return ResponseEntity.ok().build();
    }
}