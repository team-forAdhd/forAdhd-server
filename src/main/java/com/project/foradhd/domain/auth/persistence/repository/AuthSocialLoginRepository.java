package com.project.foradhd.domain.auth.persistence.repository;

import com.project.foradhd.domain.auth.persistence.entity.AuthSocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthSocialLoginRepository extends JpaRepository<AuthSocialLogin, String> {

}
