package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostScrapFilterService;
import com.project.foradhd.domain.board.business.service.PostService;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapFilterRepository;
import com.project.foradhd.global.exception.BoardNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostScrapFilterRepository scrapFilterRepository;
    private final PostScrapFilterService postScrapFilterService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostScrapFilterRepository scrapFilterRepository, PostScrapFilterService postScrapFilterService) {
        this.postRepository = postRepository;
        this.scrapFilterRepository = scrapFilterRepository;
        this.postScrapFilterService = postScrapFilterService;
    }


    @Override
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + postId + " not found"));
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post updatePost(Post post) {
        Post existingPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new BoardNotFoundException("Post with ID " + post.getId() + " not found"));

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setImages(post.getImages());
        return postRepository.save(existingPost);
    }
    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    // 내가 저장한 글
    public Page<PostScrapFilter> getScrapsByUser(String userId, Pageable pageable, SortOption sortOption) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt"); // Default sort
        switch (sortOption) {
            case NEWEST_FIRST:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
            case OLDEST_FIRST:
                sort = Sort.by(Sort.Direction.ASC, "createdAt");
                break;
            case MOST_VIEWED:
                sort = Sort.by(Sort.Direction.DESC, "post.viewCount");
                break;
            case MOST_COMMENTED:
                sort = Sort.by(Sort.Direction.DESC, "post.commentCount");
                break;
            case MOST_LIKED:
                sort = Sort.by(Sort.Direction.DESC, "post.likeCount");
                break;
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return scrapFilterRepository.findByUserId(userId, pageable);
    }

    // 나의 글
    @Override
    public Page<Post> getUserPosts(String userId, Pageable pageable, SortOption sortOption) {
        pageable = applySorting(pageable, sortOption);
        return postRepository.findByUserId(userId, pageable);
    }

    // 글 카테고리별 정렬
    @Override
    public Page<Post> listPosts(Long categoryId, Pageable pageable) {
        return postRepository.findByCategoryId(categoryId, pageable);
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
}
