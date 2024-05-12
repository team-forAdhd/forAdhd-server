package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostScrapFilterService;
import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.PostScrapFilterRepository;
import com.project.foradhd.domain.board.persistence.repository.GeneralPostRepository;
import com.project.foradhd.domain.board.web.mapper.PostScrapFilterMapper;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import com.project.foradhd.domain.board.web.dto.PostScrapFilterDto;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.global.exception.ScrapNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional
public class PostScrapFilterServiceImpl implements PostScrapFilterService {

    private final PostScrapFilterRepository scrapFilterRepository;
    private final GeneralPostRepository generalPostRepository;
    private final PostScrapFilterMapper scrapFilterMapper;
    private final GeneralPostMapper generalPostMapper;

    @Autowired
    public PostScrapFilterServiceImpl(PostScrapFilterRepository scrapFilterRepository, GeneralPostRepository generalPostRepository, PostScrapFilterMapper scrapFilterMapper, GeneralPostMapper generalPostMapper) {
        this.scrapFilterRepository = scrapFilterRepository;
        this.generalPostRepository = generalPostRepository;
        this.scrapFilterMapper = scrapFilterMapper;
        this.generalPostMapper = generalPostMapper;
    }

    @Override
    public Page<GeneralPostDto> getScrapsByUser(String userId, Pageable pageable, SortOption sortOption) {
        pageable = applySorting(pageable, sortOption);
        Page<PostScrapFilter> scrapPage = scrapFilterRepository.findByUserId(userId, pageable);
        return scrapPage.map(scrap -> generalPostMapper.toDto(scrap.getPost()));
    }

    private Pageable applySorting(Pageable pageable, SortOption sortOption) {
        Sort sort;
        switch (sortOption) {
            case OLDEST_FIRST:
                sort = Sort.by(Sort.Direction.ASC, "createdAt");
                break;
            case MOST_VIEWED:
                sort = Sort.by(Sort.Direction.DESC, "post.viewCount");
                break;
            case MOST_LIKED:
                sort = Sort.by(Sort.Direction.DESC, "post.likeCount");
                break;
            case MOST_COMMENTED:
                sort = Sort.by(Sort.Direction.DESC, "post.commentCount");
                break;
            case NEWEST_FIRST:
            default:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    @Override
    public PostScrapFilterDto createScrap(PostScrapFilterDto postScrapDto) {
        PostScrapFilter scrap = scrapFilterMapper.toEntity(postScrapDto);
        scrap = scrapFilterRepository.save(scrap);
        return scrapFilterMapper.toDto(scrap);
    }

    @Override
    public void deleteScrap(Long scrapId) {
        if (!scrapFilterRepository.existsById(scrapId)) {
            throw new ScrapNotFoundException("Scrap not found with ID: " + scrapId);
        }
        scrapFilterRepository.deleteById(scrapId);
    }
}
