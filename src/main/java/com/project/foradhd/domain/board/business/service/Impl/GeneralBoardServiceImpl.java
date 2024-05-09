package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.GeneralBoardService;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.GeneralBoardRepository;
import com.project.foradhd.domain.board.persistence.repository.GeneralCommentRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapRepository;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import com.project.foradhd.global.exception.BoardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GeneralBoardServiceImpl implements GeneralBoardService {
    private final GeneralBoardRepository boardRepository;
    private final GeneralPostMapper postMapper;
    private final GeneralCommentRepository commentRepository;
    private final PostScrapRepository scrapRepository;

    @Autowired
    public GeneralBoardServiceImpl(GeneralBoardRepository boardRepository, GeneralPostMapper postMapper, GeneralCommentRepository commentRepository, PostScrapRepository scrapRepository) {
        this.boardRepository = boardRepository;
        this.postMapper = postMapper;
        this.commentRepository = commentRepository;
        this.scrapRepository = scrapRepository;
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
        boardRepository.deleteById(postId);
    }

    @Override
    public Page<GeneralPostDto> getAllPosts(Pageable pageable) {
        return boardRepository.findAll(pageable).map(postMapper::toDto);
    }

    @Override
    public Page<GeneralPostDto> getMyPosts(String writerId, Pageable pageable) {
        return boardRepository.findByWriterId(writerId, pageable).map(postMapper::toDto);
    }


    @Override
    public Page<GeneralPostDto> getMyScraps(String userId, Pageable pageable) {
        return scrapRepository.findByUserId(userId, pageable)
                .map(scrap -> postMapper.toDto(scrap.getPost()));
    }

    @Override
    public Page<GeneralPostDto> getPostsByCategory(String categoryId, Pageable pageable) {
        return boardRepository.findByCategoryId(categoryId, pageable)
                .map(postMapper::toDto);
    }

    @Override
    public Page<GeneralPostDto> getUserPosts(String userId, Pageable pageable, SortOption sortOption) {
        pageable = applySorting(pageable, sortOption);
        Page<GeneralPost> postsPage = boardRepository.findByUserId(userId, pageable);

        if (postsPage.isEmpty()) {
            throw new BoardNotFoundException("No posts found for user with ID: " + userId);
        }
        return postsPage.map(postMapper::toDto);
    }

    private Pageable applySorting(Pageable pageable, SortOption sortOption) {
        Sort sort;
        switch (sortOption) {
            case NEWEST_FIRST:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
            case OLDEST_FIRST:
                sort = Sort.by(Sort.Direction.ASC, "createdAt");
                break;
            case MOST_VIEWED:
                sort = Sort.by(Sort.Direction.DESC, "viewCount");
                break;
            case MOST_LIKED:
                sort = Sort.by(Sort.Direction.DESC, "likeCount");
                break;
            default:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

    }

    @Override
    public Page<GeneralPostDto> listPosts(String categoryId, Pageable pageable) {
        // Category에 따른 게시물을 페이징 처리하여 조회
        Page<GeneralPost> postsPage = boardRepository.findByCategoryId(categoryId, pageable);
        if (postsPage.isEmpty()) {
            throw new BoardNotFoundException("No posts found for category: " + categoryId);
        }
        return postsPage.map(postMapper::toDto);
    }
}
