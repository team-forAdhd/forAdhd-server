package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.PostScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostScrapRepository extends JpaRepository<PostScrap, String> {
    // 사용자가 스크랩한 포스트 목록 조회
    List<PostScrap> findByUserId(String userId);
}