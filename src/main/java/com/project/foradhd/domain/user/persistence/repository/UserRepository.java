package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.enums.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndSnsUserId(Provider provider, String snsUserId);

    Optional<User> findByProviderAndEmail(Provider provider, String email);

    Optional<User> findByNickname(String nickname);
}
