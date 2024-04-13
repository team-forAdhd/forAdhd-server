package com.project.foradhd.domain.user.web.mapper;

import com.project.foradhd.domain.user.business.dto.in.SignUpData;
import com.project.foradhd.domain.user.business.dto.in.SnsSignUpData;
import com.project.foradhd.domain.user.persistence.entity.Terms;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval.UserTermsApprovalId;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.domain.user.persistence.enums.Role;
import com.project.foradhd.domain.user.web.dto.request.SignUpRequest;
import com.project.foradhd.domain.user.web.dto.request.SnsSignUpRequest;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserMapper {

    default SignUpData toSignUpData(SignUpRequest request) {
        User user = User.builder()
            .name(request.getName())
            .birth(request.getBirth())
            .gender(request.getGender())
            .email(request.getEmail())
            .role(Role.GUEST)
            .provider(Provider.FOR_A)
            .nickname(request.getNickname())
            .profileImage(request.getProfileImage())
            .isAdhd(request.getIsAdhd())
            .pushNotificationAgree(request.getPushNotificationAgree())
            .build();
        List<UserTermsApproval> userTermsApprovals = request.getTermsApprovals().stream()
            .map(termsApproval -> UserTermsApproval.builder()
                .id(mapToUserTermsApprovalId(user, termsApproval.getTermsId()))
                .approved(termsApproval.getApproved())
                .build())
            .toList();
        return new SignUpData(user, userTermsApprovals);
    }

    default UserTermsApprovalId mapToUserTermsApprovalId(User user, Long termsId) {
        Terms terms = Terms.builder().id(termsId).build();
        return new UserTermsApprovalId(user, terms);
    }

    default UserTermsApprovalId mapToUserTermsApprovalId(String userId, Long termsId) {
        User user = User.builder().id(userId).build();
        Terms terms = Terms.builder().id(termsId).build();
        return new UserTermsApprovalId(user, terms);
    }

    @Mappings({
        @Mapping(target = "user.nickname", source = "request.nickname"),
        @Mapping(target = "user.profileImage", source = "request.profileImage"),
        @Mapping(target = "user.isAdhd", source = "request.isAdhd"),
        @Mapping(target = "user.pushNotificationAgree", source = "request.pushNotificationAgree"),
        @Mapping(target = "userTermsApprovals", source = "request", qualifiedByName = "mapToUserTermsApprovals")
    })
    SnsSignUpData toSnsSignUpData(@Context String userId, SnsSignUpRequest request);

    @Named("mapToUserTermsApprovals")
    default List<UserTermsApproval> mapToUserTermsApprovals(@Context String userId, SnsSignUpRequest request) {
        return request.getTermsApprovals().stream()
            .map(termsApproval -> UserTermsApproval.builder()
                .id(mapToUserTermsApprovalId(userId, termsApproval.getTermsId()))
                .approved(termsApproval.getApproved())
                .build())
            .toList();
    }
}
