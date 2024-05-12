package com.project.foradhd.domain.board.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.project.foradhd.domain.user.persistence.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "general_post")
public class GeneralPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long writerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category categoryId;

    @Column(name = "writer_name", length = 50)
    private String writerName;

    @Column(name = "category_name", length = 100)
    private String categoryName;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "anonymous", columnDefinition = "TINYINT(1)")
    private boolean anonymous;

    @Column(name = "images", columnDefinition = "TEXT")
    private String images;

    @Column(name = "like_count", columnDefinition = "BIGINT")
    private long likeCount;

    @Column(name = "comment_count", columnDefinition = "BIGINT")
    private long commentCount;

    @Column(name = "scrap_count", columnDefinition = "BIGINT")
    private long scrapCount;

    @Column(name = "view_count", columnDefinition = "BIGINT")
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
