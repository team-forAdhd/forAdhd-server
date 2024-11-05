package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        SELECT p FROM Post p
        JOIN FETCH p.user u
        JOIN UserProfile up ON up.user.id = u.id
        WHERE p.id = :postId
        """)
    Optional<Post> findPostWithUserProfileById(@Param("postId") Long postId);

    @Query("""
        SELECT p FROM Post p
        JOIN FETCH p.user u
        JOIN UserProfile up ON up.user.id = u.id
        WHERE p.category = :category
        """)
    Page<Post> findByCategoryWithUserProfile(@Param("category") Category category, Pageable pageable);

    @Query("""
        SELECT p FROM Post p
        JOIN FETCH p.user u
        JOIN UserProfile up ON up.user.id = u.id
        WHERE u.id = :userId
        """)
    Page<Post> findByUserIdWithUserProfile(@Param("userId") String userId, Pageable pageable);

    @Query("""
        SELECT p FROM Post p
        JOIN FETCH p.user u
        JOIN UserProfile up ON up.user.id = u.id
        WHERE u.id = :userId AND p.category = :category
        """)
    Page<Post> findByUserIdAndCategoryWithUserProfile(@Param("userId") String userId, @Param("category") Category category, Pageable pageable);

    @Query("""
        SELECT p FROM Post p
        JOIN FETCH p.user u
        JOIN UserProfile up ON up.user.id = u.id
        ORDER BY p.viewCount DESC
        """)
    Page<Post> findTopPostsWithUserProfile(Pageable pageable);

    @Query("""
        SELECT p FROM Post p
        JOIN FETCH p.user u
        JOIN UserProfile up ON up.user.id = u.id
        WHERE p.category = :category
        ORDER BY p.viewCount DESC
        """)
    Page<Post> findTopPostsByCategoryWithUserProfile(@Param("category") Category category, Pageable pageable);

    @Query("""
        SELECT p FROM Post p
        JOIN FETCH p.user u
        JOIN UserProfile up ON up.user.id = u.id
        WHERE p.title LIKE %:title%
        """)
    Page<Post> findByTitleContainingWithUserProfile(@Param("title") String title, Pageable pageable);
}
