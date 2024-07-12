package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.PostLikeFilterService;
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
import com.project.foradhd.global.exception.BusinessException;
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
    private final PostLikeFilterService postLikeFilterService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        Post post = postService.getAndIncrementViewCount(postId);
        PostResponseDto response = postMapper.responsetoDto(post);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(Pageable pageable) {
        Page<Post> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts.map(postMapper::responsetoDto));
    }

    // 메인 홈 - 카테고리 별 글 조회
    @GetMapping("/category")
    public ResponseEntity<Page<PostResponseDto>> getPostsByCategory(
            @RequestParam("category") CategoryName category,
            Pageable pageable) {

        Page<Post> posts = postService.listByCategory(category, pageable);
        Page<PostResponseDto> response = posts.map(postMapper::responsetoDto);
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

    @PostMapping("/scrap/toggle")
    public ResponseEntity<?> toggleScrap(@RequestBody PostScrapFilterDto requestDto) {
        try {
            postScrapFilterService.toggleScrap(requestDto.getPostId(), requestDto.getUserId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 게시글 좋아요 토글
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> toggleLike(@AuthUserId String userId, @PathVariable Long postId) {
        try {
            postLikeFilterService.toggleLike(userId, postId);
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    // 사용자가 좋아요한 게시글 목록을 조회합니다.
    @GetMapping("/liked/{userId}")
    public ResponseEntity<Page<PostResponseDto>> getLikedPostsByUser(@PathVariable String userId, Pageable pageable) {
        Page<Post> likedPosts = postLikeFilterService.getLikedPostsByUser(userId, pageable);
        Page<PostResponseDto> responseDtos = likedPosts.map(postMapper::responsetoDto);
        return ResponseEntity.ok(responseDtos);
    }
}