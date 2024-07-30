package com.project.foradhd.domain.medicine.persistence.entity;

import com.project.foradhd.domain.user.persistence.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicine_search_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineSearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String term;
    private LocalDateTime searchedAt;
}