package com.project.foradhd.domain.board.persistence.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.global.serializer.LocalDateTimeToEpochSecondSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "notification")
public class Notification {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String message;

    private boolean isRead;

    @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
    private LocalDateTime createdAt;
}
