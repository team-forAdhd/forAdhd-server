package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostScrapService;
import com.project.foradhd.domain.board.persistence.entity.PostScrap;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.GeneralBoardRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapRepository;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.dto.PostScrapDto;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import com.project.foradhd.domain.board.web.mapper.PostScrapMapper;
import com.project.foradhd.global.exception.ScrapNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostScrapServiceImpl implements PostScrapService {

    private final PostScrapRepository scrapRepository;
    private final GeneralBoardRepository boardRepository;
    private final PostScrapMapper scrapMapper;
    private final GeneralPostMapper postMapper;

    @Autowired
    public PostScrapServiceImpl(PostScrapRepository scrapRepository, GeneralBoardRepository boardRepository, PostScrapMapper scrapMapper, GeneralPostMapper postMapper) {
        this.scrapRepository = scrapRepository;
        this.boardRepository = boardRepository;
        this.scrapMapper = scrapMapper;
        this.postMapper = postMapper;
    }


    @Override
    public Page<GeneralPostDto> getScrapsByUser(String userId, Pageable pageable, SortOption sortOption) {
        pageable = applySorting(pageable, sortOption);
        Page<PostScrap> scrapPage = scrapRepository.findByUserId(userId, pageable);
        return scrapPage.map(scrap -> postMapper.toDto(scrap.getPost()));  // GeneralPostMapper 사용
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
    public PostScrapDto createScrap(PostScrapDto postScrapDto) {
        PostScrap scrap = scrapMapper.toEntity(postScrapDto);
        scrap = scrapRepository.save(scrap);
        return scrapMapper.toDto(scrap);
    }
    @Override
    public void deleteScrap(String scrapId) {
        if (!scrapRepository.existsById(scrapId)) {
            throw new ScrapNotFoundException("Scrap not found with ID: " + scrapId);
        }
        scrapRepository.deleteById(scrapId);
    }
}
