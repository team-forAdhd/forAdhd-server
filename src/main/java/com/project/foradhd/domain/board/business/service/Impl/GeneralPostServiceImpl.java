package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.GeneralPostService;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.repository.GeneralPostRepository;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapFilterRepository;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import com.project.foradhd.global.exception.BoardNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GeneralPostServiceImpl implements GeneralPostService {
    private final GeneralPostRepository postRepository;
    private final GeneralPostMapper postMapper;
    private final CommentRepository commentRepository;
    private final PostScrapFilterRepository scrapFilterRepository;

    @Autowired
    public GeneralPostServiceImpl(GeneralPostRepository postRepository, GeneralPostMapper postMapper,
                                  CommentRepository commentRepository, PostScrapFilterRepository scrapFilterRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.commentRepository = commentRepository;
        this.scrapFilterRepository = scrapFilterRepository;
    }


    @Override
    public GeneralPostDto getPost(Long postId) {
        GeneralPost post = postRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postId + " not found"));
        return postMapper.toDto(post);
    }

    @Override
    public GeneralPostDto createPost(GeneralPostDto postDTO) {
        GeneralPost post = postMapper.toEntity(postDTO);
        post = postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public GeneralPostDto updatePost(GeneralPostDto postDTO) {
        GeneralPost existingPost = postRepository.findById(postDTO.getPostId())
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postDTO.getPostId() + " not found"));

        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        existingPost.setImages(postDTO.getImages());
        existingPost = postRepository.save(existingPost);
        return postMapper.toDto(existingPost);
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Page<GeneralPostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(postMapper::toDto);
    }

    @Override
    public Page<GeneralPostDto> getMyScraps(String userId, Pageable pageable) {
        return scrapFilterRepository.findByUserId(userId, pageable)
                .map(scrap -> postMapper.toDto(scrap.getPost()));
    }

    @Override
    public Page<GeneralPostDto> getUserPosts(String userId, Pageable pageable, SortOption sortOption) {
        pageable = applySorting(pageable, sortOption);
        return postRepository.findByUserId(Long.valueOf(userId), pageable)
                .map(postMapper::toDto);
    }

    private Pageable applySorting(Pageable pageable, SortOption sortOption) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
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
                break;
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    @Override
    public Page<GeneralPostDto> listPosts(String categoryId, Pageable pageable) {
        return postRepository.findByCategoryId(Long.valueOf(categoryId), pageable)
                .map(postMapper::toDto);
    }
}
