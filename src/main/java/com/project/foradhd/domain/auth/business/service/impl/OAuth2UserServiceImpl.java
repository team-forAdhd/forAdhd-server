package com.project.foradhd.domain.auth.business.service.impl;

import com.project.foradhd.domain.auth.business.userdetails.OAuth2Attributes;
import com.project.foradhd.domain.auth.business.userdetails.OAuth2AttributesFactory;
import com.project.foradhd.domain.auth.business.userdetails.impl.OAuth2UserImpl;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
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

    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Attributes oAuth2Attributes = OAuth2AttributesFactory.of(registrationId, nameAttributeKey, attributes);
        User snsUser = oAuth2Attributes.toEntity();
        User user = processSnsUser(snsUser);
        return new OAuth2UserImpl(attributes, nameAttributeKey, user);
    }

    private User processSnsUser(User snsUser) {
        Optional<User> userOptional = userRepository.findByProviderAndSnsUserId(
            snsUser.getProvider(), snsUser.getSnsUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.loginBySns(snsUser);
        }
        return userRepository.save(snsUser);
    }
}
