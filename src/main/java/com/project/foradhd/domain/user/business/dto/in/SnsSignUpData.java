package com.project.foradhd.domain.user.business.dto.in;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SnsSignUpData {

    private User user;
    private List<UserTermsApproval> userTermsApprovals;
}
