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
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.AuthUserId;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final PostScrapFilterService postScrapFilterService;
    private final PostScrapFilterMapper postScrapFilterMapper;
    private final PostLikeFilterService postLikeFilterService;

    // 게시글 개별 조회 api
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        PostResponseDto response = postMapper.responsetoDto(post);
        return ResponseEntity.ok(response);
    }

    // 게시글 생성 api
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthUserId String userId) {
        Post post = postMapper.toEntity(postRequestDto, userId);
        Post createdPost = postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.responsetoDto(createdPost));
    }

    // 게시글 수정 api
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        Post existingPost = postService.getPost(postId);
        Post updatedPost = Post.builder()
                .id(existingPost.getId())
                .user(existingPost.getUser())
                .category(existingPost.getCategory())
                .comments(existingPost.getComments())
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

    // 게시글 삭제 api
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // 전체 게시글 조회 api
    @GetMapping("/all")
    public ResponseEntity<PostResponseDto> getAllPosts(Pageable pageable) {
        Page<Post> postPage = postService.getAllPosts(pageable);
        List<PostResponseDto> postResponseDtoList = postPage.getContent().stream()
                .map(postMapper::responsetoDto)
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(postPage);

        PostResponseDto response = PostResponseDto.builder()
                .postList(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 카테고리별 게시글 조회 api
    @GetMapping("/category")
    public ResponseEntity<PostResponseDto> getPostsByCategory(
            @RequestParam("category") CategoryName category,
            Pageable pageable) {
        Page<Post> postPage = postService.listByCategory(category, pageable);
        List<PostResponseDto> postResponseDtoList = postPage.getContent().stream()
                .map(postMapper::responsetoDto)
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(postPage);

        PostResponseDto response = PostResponseDto.builder()
                .postList(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 마이페이지 - 나의 글 조회 api
    @GetMapping("/{userId}/my-posts")
    public ResponseEntity<PostResponseDto> getUserPosts(@AuthUserId String userId, Pageable pageable, @RequestParam SortOption sortOption) {
        Page<Post> userPosts = postService.getUserPosts(userId, pageable, sortOption);
        List<PostResponseDto> postResponseDtoList = userPosts.getContent().stream()
                .map(postMapper::responsetoDto)
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(userPosts);

        PostResponseDto response = PostResponseDto.builder()
                .postList(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
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
