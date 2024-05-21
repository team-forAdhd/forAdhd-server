package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostScrapFilterRepository extends JpaRepository<PostScrapFilter, Long> {
    Optional<PostScrapFilter> findByPostIdAndUserId(Long postId, String userId);
    Page<PostScrapFilter> findByUserId(String userId, Pageable pageable);
}
