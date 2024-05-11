package com.project.foradhd.domain.board.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.project.foradhd.domain.user.persistence.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private GeneralPost post; // 이 댓글이 속한 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "user_id")
    private User writer; // 댓글 작성자

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastModifiedAt;

    @Column(name = "anonymous", columnDefinition = "TINYINT(1)")
    private boolean anonymous;

    @Column(name = "like_count", columnDefinition = "BIGINT")
    private long likeCount;
}
