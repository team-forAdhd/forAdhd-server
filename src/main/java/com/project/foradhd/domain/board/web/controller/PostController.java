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
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.global.AuthUserId;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final UserProfileRepository userProfileRepository;
    private final PostScrapFilterService postScrapFilterService;
    private final PostScrapFilterMapper postScrapFilterMapper;
    private final PostLikeFilterService postLikeFilterService;

    // 게시글 개별 조회 api
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto.PostListResponseDto> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        PostResponseDto.PostListResponseDto response = postMapper.toPostListResponseDto(post, userProfileRepository);
        return ResponseEntity.ok(response);
    }

    // 게시글 작성 api
    @PostMapping
    public ResponseEntity<PostResponseDto.PostListResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthUserId String userId) {
        Post post = postMapper.toEntity(postRequestDto, userId);
        Post createdPost = postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toPostListResponseDto(createdPost, userProfileRepository));
    }

    // 게시글 수정 api
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto.PostListResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
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
        return ResponseEntity.ok(postMapper.toPostListResponseDto(savedPost, userProfileRepository));
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
        List<PostResponseDto.PostListResponseDto> postResponseDtoList = postPage.getContent().stream()
                .map(post -> postMapper.toPostListResponseDto(post, userProfileRepository))
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
        List<PostResponseDto.PostListResponseDto> postResponseDtoList = postPage.getContent().stream()
                .map(post -> postMapper.toPostListResponseDto(post, userProfileRepository))
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(postPage);

        PostResponseDto response = PostResponseDto.builder()
                .postList(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 내가 작성한 게시글 조회 api
    @GetMapping("/{userId}/my-posts")
    public ResponseEntity<PostResponseDto> getUserPostsByCategory(@AuthUserId String userId, @RequestParam CategoryName category, Pageable pageable, @RequestParam SortOption sortOption) {
        Page<Post> userPosts = postService.getUserPostsByCategory(userId, category, pageable, sortOption);
        List<PostResponseDto.PostListResponseDto> postResponseDtoList = userPosts.getContent().stream()
                .map(post -> postMapper.toPostListResponseDto(post, userProfileRepository))
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(userPosts);

        PostResponseDto response = PostResponseDto.builder()
                .postList(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 내가 스크랩한 게시글 조회 api
    @GetMapping("/{userId}/scraps")
    public ResponseEntity<PostScrapFilterResponseDto> getScrapsByUserAndCategory(
            @AuthUserId String userId,
            @RequestParam CategoryName category,
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "NEWEST_FIRST") SortOption sortOption) {
        Page<PostScrapFilter> scraps = postScrapFilterService.getScrapsByUserAndCategory(userId, category, pageable, sortOption);
        List<PostScrapFilterResponseDto.PostScrapFilterListResponseDto> postScrapFilterResponseDtoList = scraps.getContent().stream()
                .map(scrap -> postScrapFilterMapper.toListResponseDto(scrap, postScrapFilterService))
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(scraps);

        PostScrapFilterResponseDto response = PostScrapFilterResponseDto.builder()
                .postScrapList(postScrapFilterResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 게시글 스크랩 토글 api
    @PostMapping("/{postId}/scrap")
    public ResponseEntity<?> toggleScrap(@PathVariable Long postId, @AuthUserId String userId) {
        try {
            postScrapFilterService.toggleScrap(postId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 게시글 좋아요 토글 api
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

    // 내가 좋아요한 게시글 조회 api
    @GetMapping("/{userId}/liked")
    public ResponseEntity<PostResponseDto> getLikedPostsByUser(@AuthUserId String userId, Pageable pageable) {
        Page<Post> likedPosts = postLikeFilterService.getLikedPostsByUser(userId, pageable);
        List<PostResponseDto.PostListResponseDto> postResponseDtoList = likedPosts.getContent().stream()
                .map(post -> postMapper.toPostListResponseDto(post, userProfileRepository))
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(likedPosts);

        PostResponseDto response = PostResponseDto.builder()
                .postList(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
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

    // 게시글 검색 api
    @GetMapping("/search")
    public ResponseEntity<Page<PostResponseDto.PostListResponseDto>> searchPostsByTitle(
            @RequestParam String title,
            @AuthUserId String userId,
            Pageable pageable) {
        Page<Post> posts = postService.searchPostsByTitle(title, userId, pageable);
        List<PostResponseDto.PostListResponseDto> postResponseDtoList = posts.getContent().stream()
                .map(post -> postMapper.toPostListResponseDto(post, userProfileRepository))
                .collect(Collectors.toList());

        Page<PostResponseDto.PostListResponseDto> response = new PageImpl<>(postResponseDtoList, pageable, posts.getTotalElements());
        return ResponseEntity.ok(response);
    }

    // 최근 검색어 조회 API
    @GetMapping("/recent-searches")
    public ResponseEntity<List<String>> getRecentSearchTerms(@AuthUserId String userId) {
        List<String> recentSearchTerms = postService.getRecentSearchTerms(userId);
        return ResponseEntity.ok(recentSearchTerms);
    }
}