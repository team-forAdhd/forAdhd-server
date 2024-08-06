package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.PostLikeFilterService;
import com.project.foradhd.domain.board.business.service.PostScrapFilterService;
import com.project.foradhd.domain.board.business.service.PostSearchHistoryService;
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
import com.project.foradhd.domain.board.web.dto.response.PostSearchResponseDto;
import com.project.foradhd.domain.board.web.mapper.PostMapper;
import com.project.foradhd.domain.board.web.mapper.PostScrapFilterMapper;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.global.AuthUserId;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final PostSearchHistoryService searchHistoryService;

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
    @GetMapping("/my-posts")
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
    @GetMapping("/scraps")
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

    // 메인홈 - 실시간 랭킹
    @GetMapping("/main/top")
    public ResponseEntity<PostRankingResponseDto.PagedPostRankingResponseDto> getTopPosts(Pageable pageable) {
        Page<Post> postPage = postService.getTopPosts(pageable);

        List<PostRankingResponseDto> postList = postPage.getContent().stream()
                .map(post -> postMapper.toPostRankingResponseDto(post, userProfileRepository))
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(postPage);

        PostRankingResponseDto.PagedPostRankingResponseDto response = PostRankingResponseDto.PagedPostRankingResponseDto.builder()
                .category(null) // 실시간 랭킹이므로 특정 카테고리가 없음
                .postList(postList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 메인홈 - 카테고리별 실시간 랭킹
    @GetMapping("/main/top/category")
    public ResponseEntity<PostRankingResponseDto.PagedPostRankingResponseDto> getTopPostsByCategory(
            @RequestParam("category") CategoryName category,
            Pageable pageable) {
        Page<Post> postPage = postService.getTopPostsByCategory(category, pageable);

        List<PostRankingResponseDto> postList = postPage.getContent().stream()
                .map(post -> postMapper.toPostRankingResponseDto(post, userProfileRepository))
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(postPage);

        PostRankingResponseDto.PagedPostRankingResponseDto response = PostRankingResponseDto.PagedPostRankingResponseDto.builder()
                .category(category.name()) // 요청된 카테고리 이름
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
                .collect(Collectors.toList());

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
        List<String> recentSearchTerms = searchHistoryService.getRecentSearchTerms(userId);
        return ResponseEntity.ok(recentSearchTerms);
    }

    // 특정 검색어 삭제 API
    @DeleteMapping("/recent-searches/{id}")
    public ResponseEntity<Void> deleteSearchTermById(@PathVariable Long id) {
        searchHistoryService.deleteSearchTermById(id);
        return ResponseEntity.noContent().build();
    }
}