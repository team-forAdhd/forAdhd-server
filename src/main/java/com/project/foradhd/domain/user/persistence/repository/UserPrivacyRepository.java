package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPrivacyRepository extends JpaRepository<UserPrivacy, String> {
        Optional<UserPrivacy> findByUserId(String userId);
}
