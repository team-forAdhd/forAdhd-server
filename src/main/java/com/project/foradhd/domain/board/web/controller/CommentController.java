package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.business.service.CommentService;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.request.CreateCommentRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import com.project.foradhd.domain.board.web.mapper.CommentMapper;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.global.AuthUserId;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto.CommentListResponseDto> getComment(@PathVariable Long commentId) {
        Comment comment = commentService.getComment(commentId);
        return ResponseEntity.ok(commentMapper.commentToCommentListResponseDto(comment));
    }

    // 댓글 작성 API
    @PostMapping
    public ResponseEntity<CommentResponseDto.CommentListResponseDto> createComment(@AuthUserId String userId, @RequestBody CreateCommentRequestDto createCommentRequest) {
        Comment comment = commentMapper.createCommentRequestDtoToComment(createCommentRequest, userRepository);
        Comment createdComment = commentService.createComment(comment, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.commentToCommentListResponseDto(createdComment));
    }

    // 원댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // 개별 대댓글 삭제 API
    @DeleteMapping("/{parentId}/children/{commentId}")
    public ResponseEntity<Void> deleteChildrenComment(@PathVariable Long commentId) {
        commentService.deleteChildrenComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 수정 API
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto.CommentListResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CreateCommentRequestDto createCommentRequest) {
        Comment updatedComment = commentService.updateComment(commentId, createCommentRequest.getContent());
        return ResponseEntity.ok(commentMapper.commentToCommentListResponseDto(updatedComment));
    }

    // 나의 댓글
    @GetMapping("/{userId}/my-comments")
    public ResponseEntity<PostResponseDto> getMyCommentedPosts(@AuthUserId String userId, Pageable pageable) {
        Page<PostResponseDto.PostListResponseDto> posts = commentService.getMyCommentedPosts(userId, pageable);
        List<PostResponseDto.PostListResponseDto> postList = posts.getContent();

        PagingResponse pagingResponse = PagingResponse.from(posts);

        PostResponseDto response = PostResponseDto.builder()
                .postList(postList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 글별 댓글 모아보기
    @GetMapping("/post/{postId}")
    public ResponseEntity<CommentResponseDto> getCommentsByPost(
            @PathVariable Long postId,
            Pageable pageable,
            @RequestParam(required = false) SortOption sortOption) {

        if (sortOption == null) {
            sortOption = SortOption.NEWEST_FIRST;
        }

        Page<Comment> comments = commentService.getCommentsByPost(postId, pageable, sortOption);
        List<CommentResponseDto.CommentListResponseDto> commentList = comments.getContent().stream()
                .map(commentMapper::commentToCommentListResponseDto)
                .collect(Collectors.toList());

        PagingResponse pagingResponse = PagingResponse.from(comments);

        CommentResponseDto response = CommentResponseDto.builder()
                .commentList(commentList)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    // 댓글 좋아요 토글
    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> toggleCommentLike(@PathVariable Long commentId, @AuthUserId String userId) {
        commentService.toggleCommentLike(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
