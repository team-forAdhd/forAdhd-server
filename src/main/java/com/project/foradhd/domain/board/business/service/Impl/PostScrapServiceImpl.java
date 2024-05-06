package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostScrapService;
import com.project.foradhd.domain.board.persistence.entity.PostScrap;
import com.project.foradhd.domain.board.persistence.repository.GeneralBoardRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapRepository;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.dto.PostScrapDto;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import com.project.foradhd.domain.board.web.mapper.PostScrapMapper;
import com.project.foradhd.global.exception.ScrapNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostScrapServiceImpl implements PostScrapService {

    private final PostScrapRepository postScrapRepository;
    private final PostScrapMapper postScrapMapper;
    private final GeneralBoardRepository generalBoardRepository;

    private final GeneralPostMapper generalPostMapper;

    @Autowired
    public PostScrapServiceImpl(PostScrapRepository postScrapRepository, PostScrapMapper postScrapMapper,
                                GeneralBoardRepository generalBoardRepository, GeneralPostMapper generalPostMapper) {
        this.postScrapRepository = postScrapRepository;
        this.postScrapMapper = postScrapMapper;
        this.generalBoardRepository = generalBoardRepository;
        this.generalPostMapper = generalPostMapper;
    }

    @Override
    public List<GeneralPostDto> getScrapsByUser(String userId) {
        return postScrapRepository.findByUserId(userId).stream()
                .map(scrap -> generalBoardRepository.findById(scrap.getPostId())
                        .map(generalPostMapper::toDto)
                        .orElseThrow(() -> new RuntimeException("Post not found for ID: " + scrap.getPostId())))
                .collect(Collectors.toList());
    }


    @Override
    public PostScrapDto createScrap(PostScrapDto postScrapDto) {
        PostScrap postScrap = postScrapMapper.toEntity(postScrapDto);
        postScrap = postScrapRepository.save(postScrap);
        return postScrapMapper.toDto(postScrap);
    }

    @Override
    public PostScrapDto getScrapById(String scrapId) {
        PostScrap postScrap = postScrapRepository.findById(scrapId)
                .orElseThrow(() -> new RuntimeException("Scrap not found for ID: " + scrapId));
        return postScrapMapper.toDto(postScrap);
    }

    @Override
    public void deleteScrap(String scrapId) {
        PostScrap postScrap = postScrapRepository.findById(scrapId)
                .orElseThrow(() -> new ScrapNotFoundException(scrapId));
        postScrapRepository.delete(postScrap);
    }

}
