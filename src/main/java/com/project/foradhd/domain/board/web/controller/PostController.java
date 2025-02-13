package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.PostLikeFilterService;
import com.project.foradhd.domain.board.business.service.PostScrapFilterService;
import com.project.foradhd.domain.board.business.service.PostSearchHistoryService;
import com.project.foradhd.domain.board.business.service.PostService;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.enums.Category;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.request.PostRequestDto;
import com.project.foradhd.domain.board.web.dto.response.PostListResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostRankingResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostScrapFilterResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostSearchResponseDto;
import com.project.foradhd.domain.board.web.mapper.PostMapper;
import com.project.foradhd.domain.board.web.mapper.PostScrapFilterMapper;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.global.AuthUserId;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
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
    private final PostScrapFilterMapper postScrapFilterMapper;
    private final PostLikeFilterService postLikeFilterService;
    private final PostSearchHistoryService postSearchHistoryService;
    private final UserService userService;

    // 게시글 개별 조회 api
    @GetMapping("/{postId}")
    public ResponseEntity<PostListResponseDto.PostResponseDto> getPost(@PathVariable Long postId, @AuthUserId String userId) {
        Post post = postService.getPost(postId);
        List<String> blockedUserIdList = userService.getBlockedUserIdList(userId);
        PostListResponseDto.PostResponseDto response = postMapper.toPostResponseDto(post, userService, blockedUserIdList);
        return ResponseEntity.ok(response);
    }

    // 게시글 작성 api
    @PostMapping
    public ResponseEntity<PostListResponseDto.PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthUserId String userId) {
        Post post = postMapper.toEntity(postRequestDto, userId, userService);
        Post createdPost = postService.createPost(post);
        List<String> blockedUserIdList = userService.getBlockedUserIdList(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toPostResponseDto(createdPost, userService, blockedUserIdList));
    }

    // 게시글 수정 api
    @PutMapping("/{postId}")
    public ResponseEntity<PostListResponseDto.PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto,
                                                                        @AuthUserId String userId) {
        Post existingPost = postService.getPost(postId);
        Post updatedPost = Post.builder()
                .id(existingPost.getId())
                .user(existingPost.getUser())
                .category(existingPost.getCategory())
                .comments(existingPost.getComments())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .anonymous(postRequestDto.isAnonymous())
                .images(postRequestDto.getImages())
                .likeCount(existingPost.getLikeCount())
                .commentCount(existingPost.getCommentCount())
                .scrapCount(existingPost.getScrapCount())
                .viewCount(existingPost.getViewCount())
                .build();
        Post savedPost = postService.updatePost(updatedPost);
        List<String> blockedUserIdList = userService.getBlockedUserIdList(userId);
        return ResponseEntity.ok(postMapper.toPostResponseDto(savedPost, userService, blockedUserIdList));
    }

    // 게시글 삭제 api
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // 전체 게시글 조회 api
    @GetMapping("/all")
    public ResponseEntity<PostListResponseDto> getAllPosts(Pageable pageable, @AuthUserId String userId) {
        Page<Post> postPage = postService.getAllPosts(pageable);
        List<String> blockedUserIdList = userService.getBlockedUserIdList(userId);
        List<PostListResponseDto.PostResponseDto> postResponseDtoList = postPage.getContent().stream()
                .map(post -> postMapper.toPostResponseDto(post, userService, blockedUserIdList))
                .toList();

        PagingResponse pagingResponse = PagingResponse.from(postPage);

        PostListResponseDto response = PostListResponseDto.builder()
                .postList(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 카테고리별 게시글 조회 api
    @GetMapping("/category")
    public ResponseEntity<PostListResponseDto> getPostsByCategory(
            @RequestParam("category") Category category,
            Pageable pageable, @AuthUserId String userId) {
        Page<Post> postPage = postService.listByCategory(category, pageable);
        List<String> blockedUserIdList = userService.getBlockedUserIdList(userId);
        List<PostListResponseDto.PostResponseDto> postResponseDtoList = postPage.getContent().stream()
                .map(post -> postMapper.toPostResponseDto(post, userService, blockedUserIdList))
                .toList();

        PagingResponse pagingResponse = PagingResponse.from(postPage);

        PostListResponseDto response = PostListResponseDto.builder()
                .postList(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 내가 작성한 게시글 조회 api
    @GetMapping("/my-posts")
    public ResponseEntity<PostListResponseDto> getUserPostsByCategory(@AuthUserId String userId, @RequestParam Category category, Pageable pageable, @RequestParam SortOption sortOption) {
        Page<Post> userPosts = postService.getUserPostsByCategory(userId, category, pageable, sortOption);
        List<String> blockedUserIdList = userService.getBlockedUserIdList(userId);
        List<PostListResponseDto.PostResponseDto> postResponseDtoList = userPosts.getContent().stream()
                .map(post -> postMapper.toPostResponseDto(post, userService, blockedUserIdList))
                .toList();

        PagingResponse pagingResponse = PagingResponse.from(userPosts);

        PostListResponseDto response = PostListResponseDto.builder()
                .postList(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 내가 스크랩한 게시글 조회 api
    @GetMapping("/scraps")
    public ResponseEntity<PostScrapFilterResponseDto> getScrapsByUserAndCategory(
            @AuthUserId String userId,
            @RequestParam Category category,
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "NEWEST_FIRST") SortOption sortOption) {
        Page<PostScrapFilter> scraps = postScrapFilterService.getScrapsByUserAndCategory(userId, category, pageable, sortOption);
        List<PostScrapFilterResponseDto.PostScrapFilterListResponseDto> postScrapFilterResponseDtoList = scraps.getContent().stream()
                .map(scrap -> postScrapFilterMapper.toListResponseDto(scrap, postScrapFilterService))
                .toList();

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
        postScrapFilterService.toggleScrap(postId, userId);
        return ResponseEntity.ok().build();
    }

    // 게시글 좋아요 토글 api
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> toggleLike(@AuthUserId String userId, @PathVariable Long postId) {
        postLikeFilterService.toggleLike(userId, postId);
        return ResponseEntity.ok().build();
    }

    // 내가 좋아요한 게시글 조회 api
    @GetMapping("/liked")
    public ResponseEntity<PostListResponseDto> getLikedPostsByUser(@AuthUserId String userId, Pageable pageable) {
        Page<Post> likedPosts = postLikeFilterService.getLikedPostsByUser(userId, pageable);
        List<String> blockedUserIdList = userService.getBlockedUserIdList(userId);
        List<PostListResponseDto.PostResponseDto> postResponseDtoList = likedPosts.getContent().stream()
                .map(post -> postMapper.toPostResponseDto(post, userService, blockedUserIdList))
                .toList();

        PagingResponse pagingResponse = PagingResponse.from(likedPosts);

        PostListResponseDto response = PostListResponseDto.builder()
                .postList(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 메인홈 - 실시간 랭킹
    @GetMapping("/main/top")
    public ResponseEntity<PostRankingResponseDto.PagedPostRankingResponseDto> getTopPosts(Pageable pageable) {
        Page<Post> postPage = postService.getTopPosts(pageable);

        List<PostRankingResponseDto> postList = postPage.getContent().stream()
                .map(postMapper::toPostRankingResponseDto)
                .toList();

        PagingResponse pagingResponse = PagingResponse.from(postPage);

        PostRankingResponseDto.PagedPostRankingResponseDto response = PostRankingResponseDto.PagedPostRankingResponseDto.builder()
                .category(null)
                .postList(postList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 메인홈 - 카테고리별 실시간 랭킹
    @GetMapping("/main/top/category")
    public ResponseEntity<PostRankingResponseDto.PagedPostRankingResponseDto> getTopPostsByCategory(
            @RequestParam("category") Category category,
            Pageable pageable) {
        Page<Post> postPage = postService.getTopPostsByCategory(category, pageable);

        List<PostRankingResponseDto> postList = postPage.getContent().stream()
                .map(postMapper::toPostRankingResponseDto)
                .toList();

        PagingResponse pagingResponse = PagingResponse.from(postPage);

        PostRankingResponseDto.PagedPostRankingResponseDto response = PostRankingResponseDto.PagedPostRankingResponseDto.builder()
                .category(category.name())
                .postList(postList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 게시글 검색 api
    @GetMapping("/search")
    public ResponseEntity<PostSearchResponseDto> searchPostsByTitle(
            @RequestParam String title,
            @AuthUserId String userId,
            Pageable pageable) {
        Page<Post> posts = postService.searchPostsByTitle(title, userId, pageable);
        List<PostSearchResponseDto.PostSearchListResponseDto> postResponseDtoList = posts.getContent().stream()
                .map(postMapper::toPostSearchListResponseDto)
                .toList();

        PagingResponse pagingResponse = PagingResponse.from(posts);

        PostSearchResponseDto response = PostSearchResponseDto.builder()
                .data(postResponseDtoList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 최근 검색어 조회 API
    @GetMapping("/recent-searches")
    public ResponseEntity<List<String>> getRecentSearchTerms(@AuthUserId String userId) {
        List<String> recentSearchTerms = postSearchHistoryService.getRecentSearchTerms(userId);
        return ResponseEntity.ok(recentSearchTerms);
    }

    // 특정 검색어 삭제 API
    @DeleteMapping("/recent-searches/{id}")
    public ResponseEntity<Void> deleteSearchTermById(@PathVariable Long id) {
        postSearchHistoryService.deleteSearchTermById(id);
        return ResponseEntity.noContent().build();
    }
}
