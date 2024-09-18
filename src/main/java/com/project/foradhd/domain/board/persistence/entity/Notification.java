package com.project.foradhd.domain.board.persistence.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.audit.BaseTimeEntity;
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
public class Notification extends BaseTimeEntity {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String message;

    private boolean isRead;

    private LocalDateTime createdAt;

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}
