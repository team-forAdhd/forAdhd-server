package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.enums.PostSortOption;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;

import java.util.List;

public interface ScrapService {
    List<GeneralPostDto> getScraps(String userId, PostSortOption sortOption);
}
