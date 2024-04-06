package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval.UserTermsApprovalId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTermsApprovalRepository extends JpaRepository<UserTermsApproval, UserTermsApprovalId> {

}
