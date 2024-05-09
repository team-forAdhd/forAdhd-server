package com.project.foradhd.domain.board.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment_like_filter")
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_filter_id", length = 32, columnDefinition = "VARCHAR(32)")
    private String commentLikeFilterId;

    @Column(name = "user_id", length = 32, columnDefinition = "VARCHAR(32)")
    private String userId;

    @Column(name = "comment_id", length = 32, columnDefinition = "VARCHAR(32)")
    private String commentId;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;
}
