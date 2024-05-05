package com.project.foradhd.domain.board.persistence.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "general_post")
public class GeneralPost {

    @jakarta.persistence.Id
    @Column(name = "post_id")
    private String postId;

    @Column(name = "writer_id")
    private String writerId;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "writer_name")
    private String writerName;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "title")
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

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastModifiedAt;

    @Column(name = "view_count")
    private long viewCount;

    // Getters and Setters

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public void setScrapCount(long scrapCount) {
        this.scrapCount = scrapCount;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

}
