package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCategory(CategoryName category, Pageable pageable);
    Page<Post> findByUserId(String userId, Pageable pageable);
    Page<Post> findByUserIdAndCategory(String userId, CategoryName category, Pageable pageable);
    @Query("SELECT p FROM Post p ORDER BY p.viewCount DESC")
    Page<Post> findTopPosts(Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.category = :category ORDER BY p.viewCount DESC")
    Page<Post> findTopPostsByCategory(@Param("category") CategoryName category, Pageable pageable);
    Page<Post> findByTitleContaining(String title, Pageable pageable);
}