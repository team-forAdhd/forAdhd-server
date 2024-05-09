package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.CommentLikeService;
import com.project.foradhd.domain.board.business.service.GeneralCommentService;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
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

    // 사용자 댓글 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<GeneralCommentDto>> getUserComments(@PathVariable String userId,
                                                                   @RequestParam(defaultValue = "DESC") String sortDirection) {
        List<GeneralCommentDto> comments = service.getUserComments(userId, sortDirection);
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