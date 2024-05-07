package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.GeneralCommentService;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class GeneralCommentController {

    private final GeneralCommentService service;

    public GeneralCommentController(GeneralCommentService service) {
        this.service = service;
    }

    @GetMapping("/{Id}")
    public GeneralCommentDto getComment(@PathVariable String Id) {
        return service.getComment(Id);
    }

    @PostMapping
    public GeneralCommentDto createComment(@RequestBody GeneralCommentDto commentDto) {
        return service.createComment(commentDto);
    }

    @DeleteMapping("/{Id}")
    public void deleteComment(@PathVariable String Id) {
        service.deleteComment(Id);
    }

    @PutMapping
    public GeneralCommentDto updateComment(@RequestBody GeneralCommentDto commentDto) {
        return service.updateComment(commentDto);
    }
    @GetMapping
    public List<GeneralCommentDto> listComments() {
        return service.listComments();
    }

    // 사용자 댓글 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<GeneralCommentDto>> getUserComments(@PathVariable String userId,
                                                                   @RequestParam(defaultValue = "DESC") String sortDirection) {
        List<GeneralCommentDto> comments = service.getUserComments(userId, sortDirection);
        return ResponseEntity.ok(comments);
    }
}
