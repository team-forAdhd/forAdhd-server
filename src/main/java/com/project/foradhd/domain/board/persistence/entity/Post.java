package com.project.foradhd.domain.board.persistence.entity;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "general_post")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long writerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    private String writerName;

    private String categoryName;

    private String title;

    private String content;

    private boolean anonymous;

    private String images;

    private long likeCount;

    private long commentCount;

    private long scrapCount;

    private long viewCount;

    private String tags;

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) this.likeCount--;
    }

    public void incrementScrapCount() {
        this.scrapCount++;
    }

    public void decrementScrapCount() {
        if (this.scrapCount > 0) this.scrapCount--;
    }

}
