package com.project.foradhd.domain.board.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="category")
public class Category {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", length = 16, columnDefinition = "VARCHAR(32)")
    private String categoryId;

    @Column(name = "category_name", length = 100, columnDefinition = "VARCHAR(100)")
    private String categoryName;
}
