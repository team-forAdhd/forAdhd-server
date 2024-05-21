package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Post> findByUserId(String userId, Pageable pageable);
}
