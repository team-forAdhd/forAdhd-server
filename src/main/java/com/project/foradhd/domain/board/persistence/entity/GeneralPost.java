package com.project.foradhd.domain.board.persistence.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "general_post")
public class GeneralPost {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", length = 32, columnDefinition = "VARCHAR(32)")
    private String postId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "writer_id", length = 32, columnDefinition = "VARCHAR(32)")
    private String writerId;

    @Column(name = "category_id", length = 16, columnDefinition = "BINARY(16)")
    private String categoryId;

    @Column(name = "writer_name", length = 50, columnDefinition = "VARCHAR(50)")
    private String writerName;

    @Column(name = "category_name", length = 100, columnDefinition = "VARCHAR(100)")
    private String categoryName;

    @Column(name = "title", columnDefinition = "LONGTEXT")
    private String title;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "anonymous")
    private boolean anonymous;

    @Column(name = "images", columnDefinition = "TEXT")
    private String images;

    @Column(name = "like_count")
    private long likeCount;

    @Column(name = "comment_count")
    private long commentCount;

    @Column(name = "scrap_count")
    private long scrapCount;

    @Column(name = "view_count")
    private long viewCount;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastModifiedAt;

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) this.likeCount--;
    }
}

