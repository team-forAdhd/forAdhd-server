package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.GeneralBoardService;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
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
        GeneralPost post = boardRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postId + " not found"));
        return postMapper.toDto(post);
    }

    @Override
    public GeneralPostDto createPost(GeneralPostDto postDTO) {
        try {
            GeneralPost post = postMapper.toEntity(postDTO);
            post = boardRepository.save(post);
            return postMapper.toDto(post);
        } catch (Exception e) {
            throw new InternalSystemException(e);
        }
    }


    @Override
    public GeneralPostDto updatePost(GeneralPostDto postDTO) {
        GeneralPost existingPost = boardRepository.findById(postDTO.getPostId())
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postDTO.getPostId() + " not found"));

        if (postDTO.getTitle() == null || postDTO.getContent() == null) {
            throw new InvalidBoardOperationException("Title or content cannot be null");
        }

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

        if (!userHasAccess(post)) {
            throw new BoardAccessDeniedException("Access denied to delete post with ID " + postId);
        }

        boardRepository.deleteById(postId);
    }

    @Override
    public List<GeneralPostDto> listPosts() {
        List<GeneralPost> posts = boardRepository.findAll();
        return posts.stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    private boolean userHasAccess(GeneralPost post) {
        // 현재 인증된 사용자의 역할 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserRole = authentication.getAuthorities().iterator().next().getAuthority();

        // 관리자인 경우 항상 true 반환
        if (currentUserRole.equals("ROLE_ADMIN")) {
            return true;
        }

        // 게시글의 소유자인 경우 true 반환
        String ownerId = post.getWriterId(); // 게시글을 작성한 사람의 정보 반환
        String currentUserId = authentication.getName(); // 현재 로그인한 사용자의 ID

        return currentUserId.equals(ownerId);
    }

}
