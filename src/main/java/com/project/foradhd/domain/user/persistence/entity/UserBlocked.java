package com.project.foradhd.domain.user.persistence.entity;

import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "user_and_blocked_user_unique",
        columnNames = {"user_id", "blocked_user_id"}))
@Entity
public class UserBlocked extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_blocked_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_user_id", nullable = false)
    private User blockedUser;

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Boolean deleted = Boolean.FALSE;

    public void updateBlocked(boolean blocked) {
        this.deleted = !blocked;
    }
}
