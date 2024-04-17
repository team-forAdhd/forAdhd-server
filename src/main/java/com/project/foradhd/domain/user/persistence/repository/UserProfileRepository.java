package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    Optional<UserProfile> findByNickname(String nickname);
}
