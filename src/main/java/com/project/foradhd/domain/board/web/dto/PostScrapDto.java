package com.project.foradhd.domain.board.web.dto;

import java.time.LocalDateTime;

public class PostScrapDto {
    private String postScrapId;
    private String userId;
    private String postId;
    private LocalDateTime createdAt;

    // 기본 생성자
    public PostScrapDto() {}

    // 모든 필드를 포함한 생성자
    public PostScrapDto(String postScrapId, String userId, String postId, LocalDateTime createdAt) {
        this.postScrapId = postScrapId;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    // Getter and Setter
    public String getPostScrapId() {
        return postScrapId;
    }
    public void setPostScrapId(String postScrapId) {
        this.postScrapId = postScrapId;
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
