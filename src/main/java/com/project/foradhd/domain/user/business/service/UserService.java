package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.business.dto.in.EmailUpdateData;
import com.project.foradhd.domain.user.business.dto.in.PasswordUpdateData;
import com.project.foradhd.domain.user.business.dto.in.ProfileUpdateData;
import com.project.foradhd.domain.user.business.dto.in.PushNotificationApprovalUpdateData;
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
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.domain.user.persistence.repository.PushNotificationApprovalRepository;
import com.project.foradhd.domain.user.persistence.repository.TermsRepository;
import com.project.foradhd.domain.user.persistence.repository.UserPrivacyRepository;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.domain.user.persistence.repository.UserPushNotificationApprovalRepository;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.domain.user.persistence.repository.UserTermsApprovalRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPrivacyRepository userPrivacyRepository;
    private final UserProfileRepository userProfileRepository;
    private final TermsRepository termsRepository;
    private final UserTermsApprovalRepository userTermsApprovalRepository;
    private final PushNotificationApprovalRepository pushNotificationApprovalRepository;
    private final UserPushNotificationApprovalRepository userPushNotificationApprovalRepository;
    private final UserAuthInfoService userAuthInfoService;

    public boolean checkNickname(String nickname) {
        return userProfileRepository.findByNickname(nickname).isEmpty();
    }

    public UserProfileDetailsData getUserProfileDetails(String userId) {
        UserProfile userProfile = getUserProfileFetch(userId);
        return UserProfileDetailsData.builder()
            .userProfile(userProfile)
            .build();
    }

    @Transactional
    public void signUp(SignUpData signUpData) {
        User user = signUpData.getUser();
        UserPrivacy userPrivacy = signUpData.getUserPrivacy();
        UserProfile userProfile = signUpData.getUserProfile();
        List<UserTermsApproval> userTermsApprovals = signUpData.getUserTermsApprovals();
        List<UserPushNotificationApproval> userPushNotificationApprovals = signUpData.getUserPushNotificationApprovals();
        validateDuplicatedEmail(user.getEmail());
        validateDuplicatedNickname(userProfile.getNickname());
        validateTermsApprovals(userTermsApprovals);
        validatePushNotificationApprovals(userPushNotificationApprovals);

        userRepository.save(user);
        userPrivacyRepository.save(userPrivacy);
        userProfileRepository.save(userProfile);
        userTermsApprovalRepository.saveAll(userTermsApprovals);
        userPushNotificationApprovalRepository.saveAll(userPushNotificationApprovals);
        userAuthInfoService.signUpByPassword(user, signUpData.getPassword());
    }

    @Transactional
    public void snsSignUp(String userId, SnsSignUpData snsSignUpData) {
        UserProfile userProfile = snsSignUpData.getUserProfile();
        List<UserTermsApproval> userTermsApprovals = snsSignUpData.getUserTermsApprovals();
        List<UserPushNotificationApproval> userPushNotificationApprovals = snsSignUpData.getUserPushNotificationApprovals();
        validateDuplicatedNickname(userProfile.getNickname());
        validateTermsApprovals(userTermsApprovals);
        validatePushNotificationApprovals(userPushNotificationApprovals);

        User user = getUser(userId);
        boolean hasProfile = true;
        user.updateAsUserRole(hasProfile);

        userProfileRepository.save(userProfile);
        userTermsApprovalRepository.saveAll(userTermsApprovals);
        userPushNotificationApprovalRepository.saveAll(userPushNotificationApprovals);
    }

    @Transactional
    public void updateProfile(String userId, ProfileUpdateData profileUpdateData) {
        UserProfile newUserProfile = profileUpdateData.getUserProfile();
        validateDuplicatedNickname(newUserProfile.getNickname());
        UserProfile originUserProfile = getUserProfile(userId);
        originUserProfile.updateProfile(newUserProfile);
    }

    @Transactional
    public void updatePassword(String userId, PasswordUpdateData passwordUpdateData) {
        String prevPassword = passwordUpdateData.getPrevPassword();
        String newPassword = passwordUpdateData.getPassword();
        userAuthInfoService.validatePasswordMatches(userId, prevPassword);
        userAuthInfoService.updatePassword(userId, newPassword);
    }

    @Transactional
    public void updateEmail(String userId, EmailUpdateData emailUpdateData) {
        validateDuplicatedEmail(emailUpdateData.getEmail());
        User user = getUser(userId);
        user.updateEmail(emailUpdateData.getEmail());
    }

    @Transactional
    public void updatePushNotificationApproval(PushNotificationApprovalUpdateData pushNotificationApprovalUpdateData) {
        List<UserPushNotificationApproval> userPushNotificationApprovals = pushNotificationApprovalUpdateData.getUserPushNotificationApprovals();
        validatePushNotificationApprovals(userPushNotificationApprovals);
        userPushNotificationApprovalRepository.saveAll(userPushNotificationApprovals);
    }

    @Transactional
    public void updateTermsApprovals(TermsApprovalsUpdateData termsApprovalsUpdateData) {
        List<UserTermsApproval> userTermsApprovals = termsApprovalsUpdateData.getUserTermsApprovals();
        validateTermsApprovals(userTermsApprovals);
        userTermsApprovalRepository.saveAll(userTermsApprovals); //update or insert
    }

    @Transactional
    public void saveUserInfo(User user, UserPrivacy userPrivacy) {
        userRepository.save(user);
        userPrivacyRepository.save(userPrivacy);
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
    }

    private UserProfile getUserProfile(String userId) {
        return userProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("해당 유저 프로필이 존재하지 않습니다."));
    }

    private UserProfile getUserProfileFetch(String userId) {
        return userProfileRepository.findByUserIdFetch(userId)
            .orElseThrow(() -> new RuntimeException("해당 유저 프로필이 존재하지 않습니다."));
    }

    public Optional<User> getSignedUpUser(String email, Provider provider, String externalUserId) {
        return userRepository.findByEmailOrProviderAndExternalUserId(email, provider, externalUserId);
    }

    public boolean hasUserProfile(String userId) {
        return userRepository.findByIdWithProfile(userId)
            .isPresent();
    }

    private void validateDuplicatedEmail(String email) {
        boolean isExistingUser = userRepository.findByEmail(email).isPresent();
        if (isExistingUser) {
            throw new RuntimeException("이미 가입한 이메일입니다.");
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        boolean isDuplicatedNickname = userProfileRepository.findByNickname(nickname).isPresent();
        if (isDuplicatedNickname) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }
    }

    private void validateTermsApprovals(List<UserTermsApproval> userTermsApprovals) {
        List<Terms> termsList = termsRepository.findAll();
        for (UserTermsApproval userTermsApproval : userTermsApprovals) {
            Long termsId = userTermsApproval.getId().getTerms().getId();
            validateTerms(termsId, userTermsApproval.getApproved(), termsList);
        }
    }

    private void validateTerms(Long termsId, Boolean approved, List<Terms> termsList) {
        for (Terms terms : termsList) {
            if (!Objects.equals(terms.getId(), termsId)) continue;
            if (!terms.getRequired() || approved) return;
            throw new RuntimeException("필수 이용 약관에 동의해야 합니다."); //TODO: 예외 처리
        }
        throw new RuntimeException("존재하지 않는 이용 약관입니다.");
    }

    private void validatePushNotificationApprovals(
        List<UserPushNotificationApproval> userPushNotificationApprovals) {
        List<PushNotificationApproval> pushNotificationApprovalList = pushNotificationApprovalRepository.findAll();
        for (UserPushNotificationApproval userPushNotificationApproval : userPushNotificationApprovals) {
            Long pushNotificationApprovalId = userPushNotificationApproval.getId().getPushNotificationApproval().getId();
            validatePushNotificationApproval(pushNotificationApprovalId, pushNotificationApprovalList);
        }
    }

    private void validatePushNotificationApproval(Long pushNotificationApprovalId,
        List<PushNotificationApproval> pushNotificationApprovalList) {
        boolean isValidPushNotificationApproval = pushNotificationApprovalList.stream()
            .anyMatch(pushNotificationApproval ->
                pushNotificationApproval.getId().equals(pushNotificationApprovalId));
        if (!isValidPushNotificationApproval) {
            throw new RuntimeException("존재하지 않는 푸시 알림 동의 항목입니다.");
        }
    }
}
