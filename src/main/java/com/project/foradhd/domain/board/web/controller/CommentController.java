package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.business.service.CommentService;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.request.CreateCommentRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.board.web.mapper.CommentMapper;
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

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CreateCommentRequestDto createCommentRequest) {
        Comment comment = commentMapper.toEntity(createCommentRequest);
        Comment createdComment = commentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDto(createdComment));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CreateCommentRequestDto createCommentRequest) {
        Comment comment = commentMapper.toEntity(createCommentRequest);
        comment.setId(commentId);
        Comment updatedComment = commentService.updateComment(comment);
        return ResponseEntity.ok(commentMapper.toDto(updatedComment));
    }

    @GetMapping("/user/{writerId}")
    public ResponseEntity<Page<CommentResponseDto>> getMyComments(@PathVariable Long writerId, Pageable pageable) {
        Page<Comment> comments = commentService.getMyComments(writerId, pageable);
        return ResponseEntity.ok(comments.map(commentMapper::toDto));
    }

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

}
