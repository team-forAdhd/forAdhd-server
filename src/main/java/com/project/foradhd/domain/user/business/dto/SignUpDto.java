package com.project.foradhd.domain.user.business.dto;

import com.project.foradhd.domain.user.persistence.entity.Terms;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval.UserTermsApprovalId;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.domain.user.web.dto.request.SignUpRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public class SignUpDto {

    @Getter
    public static class In {

        private User user;
        private List<UserTermsApproval> userTermsApprovals;

        public In(SignUpRequest request) {
            this.user = User.builder()
                .name(request.getName())
                .birth(request.getBirth())
                .gender(request.getGender())
                .email(request.getEmail())
                .provider(Provider.FOR_A)
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .isAdhd(request.getIsAdhd())
                .pushNotificationAgree(request.getPushNotificationAgree())
                .build();

            this.userTermsApprovals = request.getTermsApprovals().stream()
                .map(termsApproval -> UserTermsApproval.builder()
                    .id(mapToUserTermsApprovalId(user, termsApproval.getTermsId()))
                    .approved(termsApproval.getApproved())
                    .build())
                .collect(Collectors.toList());
        }

        private static UserTermsApprovalId mapToUserTermsApprovalId(User user, Long termsId) {
            return new UserTermsApprovalId(user,
                Terms.builder()
                    .id(termsId)
                    .build());
        }
    }
}
