package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.GeneralBoardService;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.repository.GeneralBoardRepository;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GeneralBoardServiceImpl implements GeneralBoardService {

    private final GeneralBoardRepository boardRepository;
    private final GeneralPostMapper postMapper;

    @Autowired
    public GeneralBoardServiceImpl(GeneralBoardRepository boardRepository, GeneralPostMapper postMapper) {
        this.boardRepository = boardRepository;
        this.postMapper = postMapper;
    }

    @Override
    public GeneralPostDto getPost(String postId) {
        GeneralPost post = boardRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> new IllegalArgumentException("Post with ID " + postId + " not found"));
        return postMapper.toDto(post);
    }

    @Override
    public GeneralPostDto createPost(GeneralPostDto postDTO) {
        GeneralPost post = postMapper.toEntity(postDTO);
        post = boardRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public GeneralPostDto updatePost(GeneralPostDto postDTO) {
        GeneralPost existingPost = boardRepository.findById(Long.valueOf(postDTO.getPostId()))
                .orElseThrow(() -> new IllegalArgumentException("Post with ID " + postDTO.getPostId() + " not found"));
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        existingPost.setImages(postDTO.getImages());
        existingPost = boardRepository.save(existingPost);
        return postMapper.toDto(existingPost);
    }

    @Override
    public void deletePost(String postId) {
        if (!boardRepository.existsById(Long.valueOf(postId))) {
            throw new IllegalArgumentException("Post with ID " + postId + " not found");
        }
        boardRepository.deleteById(Long.valueOf(postId));
    }

    @Override
    public List<GeneralPostDto> listPosts() {
        List<GeneralPost> posts = boardRepository.findAll();
        return posts.stream().map(postMapper::toDto).collect(Collectors.toList());
    }
}
