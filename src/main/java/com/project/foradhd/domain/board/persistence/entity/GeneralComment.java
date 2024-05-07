package com.project.foradhd.domain.board.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class GeneralComment {

    @Id
    @Column(name = "comment_id", length = 32)
    private String commentId;

    @Column(name = "writer_id", length = 32)
    private String writerId;

    @Column(name = "writer_name", length = 50)
    private String writerName;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "post_type")
    private int postType;

    @Column(name = "parent_comment_id")
    private String parentCommentId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "anonymous")
    private boolean anonymous;

    @Column(name = "like_count")
    private long likeCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;
}

