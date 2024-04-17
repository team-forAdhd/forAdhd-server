package com.project.foradhd.domain.user.web.mapper;

import com.project.foradhd.domain.user.business.dto.in.EmailUpdateData;
import com.project.foradhd.domain.user.business.dto.in.PasswordUpdateData;
import com.project.foradhd.domain.user.business.dto.in.ProfileUpdateData;
import com.project.foradhd.domain.user.business.dto.in.PushNotificationAgreeUpdateData;
import com.project.foradhd.domain.user.business.dto.in.SignUpData;
import com.project.foradhd.domain.user.business.dto.in.SnsSignUpData;
import com.project.foradhd.domain.user.business.dto.in.TermsApprovalsUpdateData;
import com.project.foradhd.domain.user.business.dto.out.UserProfileDetailsData;
import com.project.foradhd.domain.user.persistence.entity.PushNotificationApproval;
import com.project.foradhd.domain.user.persistence.entity.Terms;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval;
import com.project.foradhd.domain.user.persistence.entity.UserPushNotificationApproval.UserPushNotificationApprovalId;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval.UserTermsApprovalId;
import com.project.foradhd.domain.user.persistence.enums.Role;
import com.project.foradhd.domain.user.web.dto.request.EmailUpdateRequest;
import com.project.foradhd.domain.user.web.dto.request.PasswordUpdateRequest;
import com.project.foradhd.domain.user.web.dto.request.ProfileUpdateRequest;
import com.project.foradhd.domain.user.web.dto.request.PushNotificationAgreeUpdateRequest;
import com.project.foradhd.domain.user.web.dto.request.SignUpRequest;
import com.project.foradhd.domain.user.web.dto.request.SnsSignUpRequest;
import com.project.foradhd.domain.user.web.dto.request.TermsApprovalsUpdateRequest;
import com.project.foradhd.domain.user.web.dto.response.UserProfileDetailsResponse;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserMapper {

    @Mappings({
        @Mapping(target = "email", source = "user.email"),
        @Mapping(target = "nickname", source = "user.nickname"),
        @Mapping(target = "profileImage", source = "user.profileImage"),
        @Mapping(target = "isAdhd", source = "user.isAdhd")
    })
    UserProfileDetailsResponse toUserProfileDetailsResponse(UserProfileDetailsData userProfileDetailsData);

    @Named("mapToTermsApprovals")
    @IterableMapping(qualifiedByName = "mapToTermsApproval")
    List<UserProfileDetailsResponse.TermsApprovalResponse> mapToTermsApprovals(List<UserTermsApproval> userTermsApprovals);

    @Named("mapToTermsApproval")
    @Mapping(target = "termsId", source = "id.terms.id")
    UserProfileDetailsResponse.TermsApprovalResponse mapToTermsApproval(UserTermsApproval userTermsApprovals);

    default SignUpData toSignUpData(SignUpRequest request) {
        User user = User.builder()
            .email(request.getEmail())
            .role(Role.GUEST)
            .isVerifiedEmail(Boolean.FALSE)
            .build();
        UserPrivacy userPrivacy = UserPrivacy.builder()
            .user(user)
            .name(request.getName())
            .birth(request.getBirth())
            .gender(request.getGender())
            .build();
        UserProfile userProfile = UserProfile.builder()
            .user(user)
            .nickname(request.getNickname())
            .profileImage(request.getProfileImage())
            .isAdhd(request.getIsAdhd())
            .build();
        List<UserTermsApproval> userTermsApprovals = request.getTermsApprovals().stream()
            .map(termsApproval ->
                mapToUserTermsApproval(user, termsApproval.getTermsId(), termsApproval.getApproved()))
            .toList();
        List<UserPushNotificationApproval> userPushNotificationApprovals = request.getPushNotificationApprovals().stream()
            .map(pushNotificationApproval ->
                mapToUserPushNotificationApproval(user, pushNotificationApproval.getPushNotificationApprovalId(),
                    pushNotificationApproval.getApproved()))
            .toList();

        return SignUpData.builder()
            .user(user)
            .userPrivacy(userPrivacy)
            .userProfile(userProfile)
            .password(request.getPassword())
            .userTermsApprovals(userTermsApprovals)
            .userPushNotificationApprovals(userPushNotificationApprovals)
            .build();
    }

    default UserPushNotificationApproval mapToUserPushNotificationApproval(User user, Long pushNotificationApprovalId, Boolean approved) {
        return UserPushNotificationApproval.builder()
            .id(mapToUserPushNotificationApprovalId(user, pushNotificationApprovalId))
            .approved(approved)
            .build();
    }

    default UserPushNotificationApprovalId mapToUserPushNotificationApprovalId(User user, Long pushNotificationApprovalId) {
        PushNotificationApproval pushNotificationApproval = PushNotificationApproval.builder()
            .id(pushNotificationApprovalId).build();
        return new UserPushNotificationApprovalId(user, pushNotificationApproval);
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
            .map(termsApproval ->
                mapToUserTermsApproval(userId, termsApproval.getTermsId(), termsApproval.getApproved()))
            .toList();
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

    default UserTermsApproval mapToUserTermsApproval(User user, Long termsId, Boolean approved) {
        return UserTermsApproval.builder()
            .id(mapToUserTermsApprovalId(user, termsId))
            .approved(approved)
            .build();
    }

    default UserTermsApproval mapToUserTermsApproval(String userId, Long termsId, Boolean approved) {
        return UserTermsApproval.builder()
            .id(mapToUserTermsApprovalId(userId, termsId))
            .approved(approved)
            .build();
    }

    ProfileUpdateData toProfileUpdateData(ProfileUpdateRequest request);

    PasswordUpdateData toPasswordUpdateData(PasswordUpdateRequest request);

    EmailUpdateData toEmailUpdateData(EmailUpdateRequest request);

    PushNotificationAgreeUpdateData toPushNotificationAgreeUpdateData(
        PushNotificationAgreeUpdateRequest request);

    default TermsApprovalsUpdateData toTermsApprovalsUpdateData(String userId, TermsApprovalsUpdateRequest request) {
        List<UserTermsApproval> userTermsApprovals = request.getTermsApprovals().stream()
            .map(termsApproval ->
                mapToUserTermsApproval(userId, termsApproval.getTermsId(), termsApproval.getApproved()))
            .toList();
        return new TermsApprovalsUpdateData(userTermsApprovals);
    }
}
