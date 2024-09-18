package com.project.foradhd.domain.board.persistence.entity;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_search_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String term;

    private LocalDateTime searchTime;

    @PrePersist
    protected void onCreate() {
        this.searchTime = LocalDateTime.now();
    }
}
