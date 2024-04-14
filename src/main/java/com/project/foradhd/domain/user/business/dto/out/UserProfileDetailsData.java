package com.project.foradhd.domain.user.business.dto.out;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDetailsData {

    private User user;
    private List<UserTermsApproval> userTermsApprovals;
}
