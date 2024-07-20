package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.PostLikeFilterService;
import com.project.foradhd.domain.board.business.service.PostScrapFilterService;
import com.project.foradhd.domain.board.business.service.PostService;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.PostDto;
import com.project.foradhd.domain.board.web.dto.request.PostRequestDto;
import com.project.foradhd.domain.board.web.dto.response.PostRankingResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostScrapFilterResponseDto;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final PostScrapFilterService postScrapFilterService;
    private final PostLikeFilterService postLikeFilterService;
    private final PostScrapFilterMapper postScrapFilterMapper;

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        Post post = postService.getAndIncrementViewCount(postId);
        PostResponseDto response = postMapper.responsetoDto(post);
        return ResponseEntity.ok(response);
    }

    // 글 작성 API
    @PostMapping()
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto) {
        Post post = postMapper.toEntity(postRequestDto);
        Post createdPost = postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.responsetoDto(createdPost));
    }

    // 글 수정 API
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        Post existingPost = postService.getPost(postId);
        Post updatedPost = Post.builder()
                .id(existingPost.getId())
                .writerId(existingPost.getWriterId())
                .user(existingPost.getUser())
                .category(existingPost.getCategory())
                .comments(existingPost.getComments())
                .writerName(existingPost.getWriterName())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .anonymous(existingPost.isAnonymous())
                .images(postRequestDto.getImages())
                .likeCount(existingPost.getLikeCount())
                .commentCount(existingPost.getCommentCount())
                .scrapCount(existingPost.getScrapCount())
                .viewCount(existingPost.getViewCount())
                .build();
        Post savedPost = postService.updatePost(updatedPost);
        return ResponseEntity.ok(postMapper.responsetoDto(savedPost));
    }

    // 글 삭제 API
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // 전체 글 조회 API
    @GetMapping("/all")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(Pageable pageable) {
        Page<Post> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts.map(postMapper::responsetoDto));
    }

    // 카테고리별 글 조회 API
    @GetMapping("/category")
    public ResponseEntity<Page<PostResponseDto>> getPostsByCategory(
            @RequestParam("category") CategoryName category,
            Pageable pageable) {
        Page<Post> posts = postService.listByCategory(category, pageable);
        Page<PostResponseDto> response = posts.map(postMapper::responsetoDto);
        return ResponseEntity.ok(response);
    }

    // 나의 글 조회 API
    @GetMapping("/{userId}/my-posts")
    public ResponseEntity<Page<PostDto>> getUserPosts(@AuthUserId String userId, Pageable pageable, @RequestParam SortOption sortOption) {
        Page<Post> userPosts = postService.getUserPosts(userId, pageable, sortOption);
        Page<PostDto> userPostsDto = userPosts.map(postMapper::toDto);
        return ResponseEntity.ok(userPostsDto);
    }

    // 스크랩 토글 API
    @PostMapping("/scrap/{postId}/toggle")
    public ResponseEntity<?> toggleScrap(@PathVariable Long postId, @AuthUserId String userId) {
        try {
            postScrapFilterService.toggleScrap(postId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 유저가 스크랩 한 글 조회
    @GetMapping("/scrap")
    public ResponseEntity<Page<PostScrapFilterResponseDto>> getScrapsByUser(
            @AuthUserId String userId,
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "NEWEST_FIRST") SortOption sortOption) {
        Page<PostScrapFilter> scraps = postScrapFilterService.getScrapsByUser(userId, pageable, sortOption);
        Page<PostScrapFilterResponseDto> responseDtos = scraps.map(scrap -> postScrapFilterMapper.toResponseDto(scrap, postScrapFilterService));
        return ResponseEntity.ok(responseDtos);
    }

    // 좋아요 토글 API
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

    // 좋아요한 글 조회 API
    @GetMapping("/liked/{userId}")
    public ResponseEntity<Page<PostResponseDto>> getLikedPostsByUser(@PathVariable String userId, Pageable pageable) {
        Page<Post> likedPosts = postLikeFilterService.getLikedPostsByUser(userId, pageable);
        Page<PostResponseDto> responseDtos = likedPosts.map(postMapper::responsetoDto);
        return ResponseEntity.ok(responseDtos);
    }

    // 메인홈 - 실시간 랭킹순
    @GetMapping("/main/top")
    public ResponseEntity<List<PostRankingResponseDto>> getTopPosts(Pageable pageable) {
        List<PostRankingResponseDto> topPosts = postService.getTopPosts(pageable);
        return ResponseEntity.ok(topPosts);
    }

    // 메인홈 - 카테고리별 랭킹순
    @GetMapping("/main/top/{category}")
    public ResponseEntity<List<PostRankingResponseDto>> getTopPostsByCategory(
            @PathVariable CategoryName category,
            Pageable pageable) {
        List<PostRankingResponseDto> topPosts = postService.getTopPostsByCategory(category, pageable);
        return ResponseEntity.ok(topPosts);
    }
}
