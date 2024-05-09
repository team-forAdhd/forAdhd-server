package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.CommentLikeService;
import com.project.foradhd.domain.board.business.service.GeneralCommentService;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class GeneralCommentController {

    private final GeneralCommentService service;
    private final CommentLikeService commentLikeService;

    public GeneralCommentController(GeneralCommentService service, CommentLikeService commentLikeService) {
        this.service = service;
        this.commentLikeService = commentLikeService;
    }

    @GetMapping("/{commentId}")
    public GeneralCommentDto getComment(@PathVariable String commentId) {
        return service.getComment(commentId);
    }

    @PostMapping
    public GeneralCommentDto createComment(@RequestBody GeneralCommentDto commentDto) {
        return service.createComment(commentDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable String commentId) {
        service.deleteComment(commentId);
    }

    @PutMapping
    public GeneralCommentDto updateComment(@RequestBody GeneralCommentDto commentDto) {
        return service.updateComment(commentDto);
    }


    // 내 댓글 조회
    @GetMapping("/user/{writerId}")
    public ResponseEntity<Page<GeneralCommentDto>> getMyComments(@PathVariable String writerId, Pageable pageable) {
        Page<GeneralCommentDto> comments = service.getMyComments(writerId, pageable);
        return ResponseEntity.ok(comments);
    }

    // 특정 게시글의 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<GeneralCommentDto>> getCommentsByPost(@PathVariable String postId, Pageable pageable, SortOption sortOption) {
        Page<GeneralCommentDto> comments = service.getCommentsByPost(postId, pageable, sortOption);
        return ResponseEntity.ok(comments);
    }

    // 댓글 좋아요 기능
    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> toggleCommentLike(@PathVariable String commentId, @RequestParam String userId) {
        try {
            commentLikeService.toggleLike(userId, commentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error toggling like: " + e.getMessage());
        }
    }
}