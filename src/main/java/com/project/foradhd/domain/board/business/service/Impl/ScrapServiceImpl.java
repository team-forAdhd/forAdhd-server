package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.ScrapService;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.entity.Scrap;
import com.project.foradhd.domain.board.persistence.enums.PostSortOption;
import com.project.foradhd.domain.board.persistence.repository.GeneralBoardRepository;
import com.project.foradhd.domain.board.persistence.repository.ScrapRepository;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrapServiceImpl implements ScrapService {
    private final ScrapRepository scrapRepository;
    private final GeneralBoardRepository boardRepository;
    private final GeneralPostMapper postMapper;

    @Autowired
    public ScrapServiceImpl(ScrapRepository scrapRepository, GeneralBoardRepository boardRepository, GeneralPostMapper postMapper) {
        this.scrapRepository = scrapRepository;
        this.boardRepository = boardRepository;
        this.postMapper = postMapper;
    }

    @Override
    public List<GeneralPostDto> getScraps(String userId, PostSortOption sortOption) {
        List<Scrap> scraps = scrapRepository.findByUserId(userId);
        List<String> postIds = scraps.stream().map(Scrap::getPostId).collect(Collectors.toList());
        List<GeneralPost> posts = boardRepository.findAllById(postIds);
        return sortPosts(posts, sortOption).stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    private List<GeneralPost> sortPosts(List<GeneralPost> posts, PostSortOption sortOption) {
        switch (sortOption) {
            case NEWEST_FIRST:
                return posts.stream().sorted(Comparator.comparing(GeneralPost::getCreatedAt).reversed()).collect(Collectors.toList());
            case OLDEST_FIRST:
                return posts.stream().sorted(Comparator.comparing(GeneralPost::getCreatedAt)).collect(Collectors.toList());
            case MOST_VIEWED:
                return posts.stream().sorted(Comparator.comparing(GeneralPost::getViewCount).reversed()).collect(Collectors.toList());
            case MOST_LIKED:
                return posts.stream().sorted(Comparator.comparing(GeneralPost::getLikeCount).reversed()).collect(Collectors.toList());
            default:
                return posts;
        }
    }
}