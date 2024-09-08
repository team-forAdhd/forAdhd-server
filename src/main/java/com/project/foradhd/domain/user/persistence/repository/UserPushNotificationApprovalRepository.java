package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval;
import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval.UserPushNotificationApprovalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPushNotificationApprovalRepository extends JpaRepository<UserPushNotificationApproval, UserPushNotificationApprovalId> {

    @Modifying
    @Query("delete from UserPushNotificationApproval upna where upna.id.user.id = :userId")
    void deleteByUserId(@Param("userId") String userId);
}
