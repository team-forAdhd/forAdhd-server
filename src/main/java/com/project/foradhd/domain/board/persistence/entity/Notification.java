package com.project.foradhd.domain.board.persistence.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.serializer.LocalDateTimeToEpochSecondSerializer;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String message;

    private boolean isRead;

    @JsonSerialize(using = LocalDateTimeToEpochSecondSerializer.class)
    private LocalDateTime createdAt;
}
