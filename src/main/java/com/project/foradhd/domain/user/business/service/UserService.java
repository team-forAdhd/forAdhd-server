package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.business.dto.in.EmailUpdateData;
import com.project.foradhd.domain.user.business.dto.in.PasswordUpdateData;
import com.project.foradhd.domain.user.business.dto.in.ProfileUpdateData;
import com.project.foradhd.domain.user.business.dto.in.PushNotificationAgreeUpdateData;
import com.project.foradhd.domain.user.business.dto.in.SignUpData;
import com.project.foradhd.domain.user.business.dto.in.SnsSignUpData;
import com.project.foradhd.domain.user.business.dto.in.TermsApprovalsUpdateData;
import com.project.foradhd.domain.user.persistence.entity.Terms;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import com.project.foradhd.domain.user.persistence.repository.TermsRepository;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.domain.user.persistence.repository.UserTermsApprovalRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TermsRepository termsRepository;
    private final UserTermsApprovalRepository userTermsApprovalRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkNickname(String nickname) {
        return userRepository.findByNickname(nickname).isEmpty();
    }

    @Transactional
    public void signUp(SignUpData signUpData, String password) {
        User user = signUpData.getUser();
        List<UserTermsApproval> userTermsApprovals = signUpData.getUserTermsApprovals();
        validateNewUser(user);
        validateTermsApprovals(userTermsApprovals);

        String encodedPassword = passwordEncoder.encode(password);
        user.updateEncodedPassword(encodedPassword);
        userRepository.save(user);
        userTermsApprovalRepository.saveAll(userTermsApprovals);
    }

    @Transactional
    public void snsSignUp(String userId, SnsSignUpData snsSignUpData) {
        User user = snsSignUpData.getUser();
        List<UserTermsApproval> userTermsApprovals = snsSignUpData.getUserTermsApprovals();
        validateDuplicatedNickname(user.getNickname());
        validateTermsApprovals(userTermsApprovals);

        User snsUser = getUser(userId);
        snsUser.snsSignUp(user);
        userTermsApprovalRepository.saveAll(userTermsApprovals);
    }

    @Transactional
    public void updateProfile(String userId, ProfileUpdateData profileUpdateData) {
        User user = getUser(userId);
        validateDuplicatedNickname(profileUpdateData.getNickname());
        user.updateProfile(profileUpdateData.getNickname(), profileUpdateData.getProfileImage(),
            profileUpdateData.getIsAdhd());
    }

    @Transactional
    public void updatePassword(String userId, PasswordUpdateData passwordUpdateData) {
        User user = getUser(userId);
        validatePasswordMatches(passwordUpdateData.getPrevPassword(), user.getPassword());
        String encodedNewPassword = passwordEncoder.encode(passwordUpdateData.getPassword());
        user.updateEncodedPassword(encodedNewPassword);
    }

    @Transactional
    public void updateEmail(String userId, EmailUpdateData emailUpdateData) {
        validateDuplicatedEmail(Provider.FOR_A, emailUpdateData.getEmail());
        User user = getUser(userId);
        user.updateEmail(emailUpdateData.getEmail());
    }

    @Transactional
    public void updatePushNotificationAgree(String userId,
        PushNotificationAgreeUpdateData pushNotificationAgreeUpdateData) {
        User user = getUser(userId);
        user.updatePushNotificationAgree(pushNotificationAgreeUpdateData.getPushNotificationAgree());
    }

    @Transactional
    public void updateTermsApprovals(TermsApprovalsUpdateData termsApprovalsUpdateData) {
        List<UserTermsApproval> userTermsApprovals = termsApprovalsUpdateData.getUserTermsApprovals();
        validateTermsApprovals(userTermsApprovals);
        userTermsApprovalRepository.saveAll(userTermsApprovals); //update or insert
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
    }

    private void validateNewUser(User user) {
        validateDuplicatedEmail(Provider.FOR_A, user.getEmail());
        validateDuplicatedNickname(user.getNickname());
    }

    private void validateDuplicatedEmail(Provider provider, String email) {
        boolean isExistingUser = userRepository.findByProviderAndEmail(provider, email).isPresent();
        if (isExistingUser) {
            throw new RuntimeException("이미 가입한 이메일입니다.");
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        boolean isDuplicatedNickname = userRepository.findByNickname(nickname).isPresent();
        if (isDuplicatedNickname) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }
    }

    private void validatePasswordMatches(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
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
        throw new RuntimeException("존재하지 않은 이용 약관입니다.");
    }
}
