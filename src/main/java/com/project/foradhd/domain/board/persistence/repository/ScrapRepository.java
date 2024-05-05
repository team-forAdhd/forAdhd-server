package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    // 특정 사용자의 모든 스크랩을 찾는 메소드
    List<Scrap> findByUserId(String userId);

    // 사용자가 스크랩한 특정 게시글을 찾는 메소드 (선택적 사용)
    List<Scrap> findByUserIdAndPostId(String userId, String postId);
}