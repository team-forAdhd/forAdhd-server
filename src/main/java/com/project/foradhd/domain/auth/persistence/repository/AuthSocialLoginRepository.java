package com.project.foradhd.domain.auth.persistence.repository;

import com.project.foradhd.domain.auth.persistence.entity.AuthSocialLogin;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthSocialLoginRepository extends JpaRepository<AuthSocialLogin, String> {

    Optional<AuthSocialLogin> findByProviderAndExternalUserId(Provider provider, String externalUserId);

    @Modifying
    @Query("delete from AuthSocialLogin asl where asl.user.id = :userId")
    void deleteByUserId(@Param("userId") String userId);
}
