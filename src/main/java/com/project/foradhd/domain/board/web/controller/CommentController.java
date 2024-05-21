package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.CommentDto;
import com.project.foradhd.domain.board.business.service.CommentService;
import com.project.foradhd.domain.board.web.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable Long commentId) {
        Comment comment = commentService.getComment(commentId);
        return ResponseEntity.ok(commentMapper.toDto(comment));
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        Comment comment = commentMapper.toEntity(commentDto);
        Comment createdComment = commentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDto(createdComment));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setId(commentId);
        Comment updatedComment = commentService.updateComment(comment);
        return ResponseEntity.ok(commentMapper.toDto(updatedComment));
    }

    @GetMapping("/{writerId}/my-comments")
    public ResponseEntity<Page<CommentDto>> getMyComments(@PathVariable Long writerId, Pageable pageable) {
        Page<Comment> comments = commentService.getMyComments(writerId, pageable);
        return ResponseEntity.ok(comments.map(commentMapper::toDto));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentDto>> getCommentsByPost(@PathVariable Long postId, Pageable pageable, SortOption sortOption) {
        Page<Comment> comments = commentService.getCommentsByPost(postId, pageable, sortOption);
        return ResponseEntity.ok(comments.map(commentMapper::toDto));
    }
}
