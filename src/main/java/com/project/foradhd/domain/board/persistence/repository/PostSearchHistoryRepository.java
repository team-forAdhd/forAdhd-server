package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.PostSearchHistory;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostSearchHistoryRepository extends JpaRepository<PostSearchHistory, Long> {

    List<PostSearchHistory> findByUserOrderByCreatedAtDesc(User user);
}