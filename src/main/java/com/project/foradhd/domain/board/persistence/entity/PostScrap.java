package com.project.foradhd.domain.board.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_scrap")
public class PostScrap {

    @jakarta.persistence.Id
    private String postScrapFilterId;
    private String userId;
    private String postId;
    private LocalDateTime createdAt;

    // Getters and Setters
    public String getPostScrapFilterId() {
        return postScrapFilterId;
    }

    public void setPostScrapFilterId(String postScrapFilterId) {
        this.postScrapFilterId = postScrapFilterId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}