package com.project.foradhd.domain.board.persistence.entity;

import com.project.foradhd.domain.board.persistence.enums.CategoryName;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CategoryName category;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    private String title;

    private String content;

    private boolean anonymous;

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    private List<String> images;

    private long likeCount;

    private long commentCount;

    private long scrapCount;

    private long viewCount;

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

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
