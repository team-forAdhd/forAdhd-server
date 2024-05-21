package com.project.foradhd.domain.board.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="category")
public class Category {
    @Id
    @Column(name = "category_id")
    private Long id;

    private String categoryName;
}
