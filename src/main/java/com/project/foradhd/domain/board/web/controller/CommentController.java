package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.business.service.CommentService;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.request.CreateCommentRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import com.project.foradhd.domain.board.web.mapper.CommentMapper;
import com.project.foradhd.global.AuthUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long commentId) {
        Comment comment = commentService.getComment(commentId);
        return ResponseEntity.ok(commentMapper.toDto(comment));
    }

    // 댓글 작성 API
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@AuthUserId String userId, @RequestBody CreateCommentRequestDto createCommentRequest) {
        Comment comment = commentMapper.toEntity(createCommentRequest);
        Comment createdComment = commentService.createComment(comment, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDto(createdComment));
    }

    //원댓글 삭제 API
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

    //댓글 수정 API
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CreateCommentRequestDto createCommentRequest) {
        Comment updatedComment = commentService.updateComment(commentId, createCommentRequest.getContent());
        return ResponseEntity.ok(commentMapper.toDto(updatedComment));
    }

    //나의 댓글
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostResponseDto>> getMyCommentedPosts(@AuthUserId String userId, Pageable pageable) {
        Page<PostResponseDto> posts = commentService.getMyCommentedPosts(userId, pageable);
        return ResponseEntity.ok(posts);
    }

    //글별 댓글 모아보기
    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByPost(
            @PathVariable Long postId,
            Pageable pageable,
            @RequestParam(required = false) SortOption sortOption) {

        if (sortOption == null) {
            sortOption = SortOption.NEWEST_FIRST;
        }

        Page<Comment> comments = commentService.getCommentsByPost(postId, pageable, sortOption);
        return ResponseEntity.ok(comments.map(commentMapper::toDto));
    }

    //댓글 좋아요 토글
    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> toggleCommentLike(@PathVariable Long commentId, @AuthUserId String userId) {
        commentService.toggleCommentLike(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
