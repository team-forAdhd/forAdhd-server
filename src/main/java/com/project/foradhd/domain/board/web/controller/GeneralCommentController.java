package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.GeneralCommentService;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class GeneralCommentController {

    private final GeneralCommentService service;

    public GeneralCommentController(GeneralCommentService service) {
        this.service = service;
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
}
