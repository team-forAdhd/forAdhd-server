package com.project.foradhd.domain.user.business.service;

import com.project.foradhd.domain.user.business.dto.in.SignUpData;
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

    @Transactional
    public void signUp(SignUpData signUpData, String password) {
        User user = signUpData.getUser();
        List<UserTermsApproval> userTermsApprovals = signUpData.getUserTermsApprovals();
        validateNewUser(user);
        validateTermsApprovals(userTermsApprovals);

        String encodedPassword = passwordEncoder.encode(password);
        user.updateEncodedPassword(encodedPassword);
        userRepository.save(user);
        userTermsApprovalRepository.saveAll(userTermsApprovals); //TODO: UserTermsApproval 내 복합키로 인해 insert 전 select 쿼리 발생
    }

    private void validateNewUser(User user) {
        boolean isExistingUser = userRepository.findByProviderAndEmail(Provider.FOR_A, user.getEmail())
            .isPresent();
        if (isExistingUser) {
            throw new RuntimeException("이미 가입한 이메일입니다.");
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
