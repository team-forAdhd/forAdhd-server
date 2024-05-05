package com.project.foradhd.domain.board.web.controller;

import com.project.foradhd.domain.board.business.service.GeneralBoardService;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/posts")
public class GeneralBoardController {
    private final GeneralBoardService service;

    public GeneralBoardController(GeneralBoardService service) {
        this.service = service;
    }

    @GetMapping("/{postId}")
    public GeneralPostDto getPost(@PathVariable String postId) {
        return service.getPost(postId);
    }

    @PostMapping
    public GeneralPostDto createPost(@RequestBody GeneralPostDto postDTO) {
        return service.createPost(postDTO);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable String postId) {
        service.deletePost(postId);
    }

    @PutMapping
    public GeneralPostDto updatePost(@RequestBody GeneralPostDto postDTO) {
        return service.updatePost(postDTO);
    }

    @GetMapping
    public List<GeneralPostDto> listPosts() {
        return service.listPosts();
    }
}
