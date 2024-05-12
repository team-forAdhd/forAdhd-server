package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostScrapFilterRepository extends PagingAndSortingRepository<PostScrapFilter, Long> {
    // 사용자가 스크랩한 포스트 목록 조회
    Page<PostScrapFilter> findByUserId(String userId, Pageable pageable);
    void deleteById(Long scrapId);
    PostScrapFilter save(PostScrapFilter scrap);
    boolean existsById(Long scrapId);
}
