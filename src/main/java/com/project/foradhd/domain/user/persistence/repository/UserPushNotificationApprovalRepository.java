package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval;
import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval.UserPushNotificationApprovalId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPushNotificationApprovalRepository extends JpaRepository<UserPushNotificationApproval, UserPushNotificationApprovalId> {

}
