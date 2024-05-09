package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.GeneralComment;
import com.project.foradhd.domain.board.persistence.entity.PostScrap;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostScrapRepository extends PagingAndSortingRepository<GeneralComment, String> {
    // 사용자가 스크랩한 포스트 목록 조회
    Page<PostScrap> findByUserId(String userId, Pageable pageable);

    void deleteById(String scrapId);

    PostScrap save(PostScrap scrap);

    Page<GeneralPostDto> getScrapsByUser(String userId, Pageable pageable, SortOption sortOption);

    boolean existsById(String scrapId);
}