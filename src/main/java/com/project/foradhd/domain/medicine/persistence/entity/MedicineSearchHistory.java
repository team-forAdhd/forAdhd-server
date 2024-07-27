package com.project.foradhd.domain.medicine.persistence.entity;

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

    private String userId;
    private String term;
    private LocalDateTime searchedAt;
}