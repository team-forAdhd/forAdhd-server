package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.GeneralBoardService;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.enums.PostSortOption;
import com.project.foradhd.domain.board.persistence.repository.GeneralBoardRepository;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import com.project.foradhd.global.exception.BoardAccessDeniedException;
import com.project.foradhd.global.exception.BoardNotFoundException;
import com.project.foradhd.global.exception.InternalSystemException;
import com.project.foradhd.global.exception.InvalidBoardOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.foradhd.domain.board.persistence.enums.PostSortOption.*;

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
        GeneralPost post = boardRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postId + " not found"));
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
        GeneralPost existingPost = boardRepository.findById(postDTO.getPostId())
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postDTO.getPostId() + " not found"));

        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        existingPost.setImages(postDTO.getImages());
        existingPost = boardRepository.save(existingPost);
        return postMapper.toDto(existingPost);
    }

    @Override
    public void deletePost(String postId) {
        GeneralPost post = boardRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postId + " not found"));
        boardRepository.deleteById(postId);
    }

    @Override
    public List<GeneralPostDto> listPosts() {
        List<GeneralPost> posts = boardRepository.findAll();
        return posts.stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<GeneralPostDto> getUserPosts(String userId, PostSortOption sortOption) {
        List<GeneralPost> userPosts = boardRepository.findByWriterId(userId);
        return userPosts.stream()
                .sorted(getComparator(sortOption))
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    private Comparator<GeneralPost> getComparator(PostSortOption sortOption) {
        switch (sortOption) {
            case NEWEST_FIRST:
                return Comparator.comparing(GeneralPost::getCreatedAt).reversed();
            case OLDEST_FIRST:
                return Comparator.comparing(GeneralPost::getCreatedAt);
            case MOST_VIEWED:
                return Comparator.comparing(GeneralPost::getViewCount).reversed();
            case MOST_LIKED:
                return Comparator.comparing(GeneralPost::getLikeCount).reversed();
            default:
                return Comparator.comparing(GeneralPost::getCreatedAt).reversed();
        }
    }
}