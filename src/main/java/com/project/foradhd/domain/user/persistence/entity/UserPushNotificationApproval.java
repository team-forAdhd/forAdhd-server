package com.project.foradhd.domain.user.persistence.entity;

import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserPushNotificationApproval extends BaseTimeEntity {

    @EmbeddedId
    private UserPushNotificationApprovalId id;

    @Builder.Default
    @ColumnDefault("0")
    @Column(nullable = false)
    private Boolean approved = Boolean.FALSE;

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Embeddable
    public static class UserPushNotificationApprovalId implements Serializable {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "push_notification_approval_id")
        private PushNotificationApproval pushNotificationApproval;
    }
}
