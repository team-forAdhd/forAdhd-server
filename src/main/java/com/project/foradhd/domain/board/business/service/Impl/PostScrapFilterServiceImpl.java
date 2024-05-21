package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostScrapFilterService;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapFilterRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostScrapFilterServiceImpl implements PostScrapFilterService {

    private final PostScrapFilterRepository scrapFilterRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public void toggleScrap(Long postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userService.getUser(userId);

        scrapFilterRepository.findByPostIdAndUserId(postId, userId)
                .ifPresentOrElse(
                        scrap -> {
                            scrapFilterRepository.delete(scrap);
                            post.decrementScrapCount();
                        },
                        () -> {
                            PostScrapFilter newScrap = PostScrapFilter.builder()
                                    .post(post)
                                    .user(user)
                                    .build();
                            scrapFilterRepository.save(newScrap);
                            post.incrementScrapCount();
                        }
                );
    }

    public Page<PostScrapFilter> getScrapsByUser(String userId, Pageable pageable, SortOption sortOption) {
        // 정렬 옵션을 기반으로 Pageable 객체 생성
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), getSortByOption(sortOption));
        // 사용자 ID와 정렬된 Pageable 객체를 사용하여 데이터 조회
        return scrapFilterRepository.findByUserId(userId, sortedPageable);
    }

    private Sort getSortByOption(SortOption option) {
        switch (option) {
            case NEWEST_FIRST:
                return Sort.by("post.createdAt").descending();
            case OLDEST_FIRST:
                return Sort.by("post.createdAt").ascending();
            case MOST_VIEWED:
                return Sort.by("post.viewCount").descending();
            case MOST_COMMENTED:
                return Sort.by("post.commentCount").descending();
            case MOST_LIKED:
                return Sort.by("post.likeCount").descending();
            default:
                return Sort.unsorted();
        }
    }
}
