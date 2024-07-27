package com.project.foradhd.domain.board.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_search_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String term;

    private LocalDateTime searchTime;

    @PrePersist
    protected void onCreate() {
        this.searchTime = LocalDateTime.now();
    }
}
