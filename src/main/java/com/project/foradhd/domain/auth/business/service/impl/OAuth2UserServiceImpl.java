package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.userdetails.OAuth2Attributes;
import com.project.foradhd.domain.auth.business.userdetails.OAuth2AttributesFactory;
import com.project.foradhd.domain.auth.business.userdetails.impl.OAuth2UserImpl;
import com.project.foradhd.domain.auth.persistence.entity.AuthSocialLogin;
import com.project.foradhd.domain.auth.persistence.repository.AuthSocialLoginRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserService userService;
    private final AuthSocialLoginRepository authSocialLoginRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Attributes oAuth2Attributes = OAuth2AttributesFactory.valueOf(registrationId, nameAttributeKey, attributes);
        User user = getSocialLoginedUser(oAuth2Attributes);
        return new OAuth2UserImpl(attributes, nameAttributeKey, user);
    }

    private User getSocialLoginedUser(OAuth2Attributes oAuth2Attributes) {
        String email = oAuth2Attributes.getEmail();
        Provider provider = oAuth2Attributes.getProvider();
        String externalUserId = oAuth2Attributes.getId();
        Optional<User> userOptional = userService.getSignedUpUser(email, provider, externalUserId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return loginSocialUser(user, oAuth2Attributes);
        } else {
            return signUpSocialUser(oAuth2Attributes);
        }
    }

    private User loginSocialUser(User user, OAuth2Attributes oAuth2Attributes) {
        boolean isVerifiedEmail = user.getIsVerifiedEmail() || oAuth2Attributes.getIsVerifiedEmail();
        boolean hasProfile = userService.hasUserProfile(user.getId());
        user.updateAsUserRole(isVerifiedEmail, hasProfile);

        Provider provider = oAuth2Attributes.getProvider();
        String externalUserId = oAuth2Attributes.getId();
        boolean existsSocialUser = authSocialLoginRepository.findByProviderAndExternalUserId(provider, externalUserId)
            .isPresent();
        if (!existsSocialUser) {
            AuthSocialLogin authSocialLogin = oAuth2Attributes.toAuthSocialLoginEntity(user);
            authSocialLoginRepository.save(authSocialLogin);
        }
        return user;
    }

    private User signUpSocialUser(OAuth2Attributes oAuth2Attributes) {
        User user = oAuth2Attributes.toUserEntity();
        UserPrivacy userPrivacy = oAuth2Attributes.toUserPrivacyEntity(user);
        AuthSocialLogin authSocialLogin = oAuth2Attributes.toAuthSocialLoginEntity(user);
        userService.saveUserInfo(user, userPrivacy);
        authSocialLoginRepository.save(authSocialLogin);
        return user;
    }
}
