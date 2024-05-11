package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.CommentDto;
import com.project.foradhd.domain.board.business.service.CommentService;
import com.project.foradhd.domain.board.business.service.CommentLikeFilterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeFilterService commentLikeFilterService;
    private final CommentDto commentDto;

    public CommentController(CommentService commentService, CommentLikeFilterService commentLikeFilterService, CommentDto commentDto) {
        this.commentService = commentService;
        this.commentLikeFilterService = commentLikeFilterService;
        this.commentDto = commentDto;
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(@PathVariable Long commentId) {
        return commentService.getComment(commentId);
    }

    @PostMapping
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PutMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        commentDto.setCommentId(commentId);
        return commentService.updateComment(commentDto);
    }

    @GetMapping("/user/{writerId}")
    public ResponseEntity<Page<CommentDto>> getMyComments(@PathVariable Long writerId, Pageable pageable) {
        Page<CommentDto> comments = commentService.getMyComments(writerId, pageable);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<CommentDto>> getCommentsByPost(@PathVariable Long postId, Pageable pageable, SortOption sortOption) {
        Page<CommentDto> comments = commentService.getCommentsByPost(postId, pageable, sortOption);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> toggleCommentLike(@PathVariable Long commentId, @RequestParam Long userId) {
        try {
            commentLikeFilterService.toggleLike(userId, commentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error toggling like: " + e.getMessage());
        }
    }
}
