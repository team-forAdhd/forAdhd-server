package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.PushNotificationApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushNotificationApprovalRepository extends JpaRepository<PushNotificationApproval, Long> {

}
