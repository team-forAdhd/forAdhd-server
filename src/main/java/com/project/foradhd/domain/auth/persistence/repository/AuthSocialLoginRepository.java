package com.project.foradhd.domain.auth.persistence.repository;

import com.project.foradhd.domain.auth.persistence.entity.AuthSocialLogin;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthSocialLoginRepository extends JpaRepository<AuthSocialLogin, String> {

    Optional<AuthSocialLogin> findByProviderAndExternalUserId(Provider provider, String externalUserId);
}
