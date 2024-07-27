package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.PostSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostSearchHistoryRepository extends JpaRepository<PostSearchHistory, Long> {
    List<PostSearchHistory> findByUserIdOrderBySearchTimeDesc(String userId);
}
