package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserBlocked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBlockedRepository extends JpaRepository<UserBlocked, Long> {

    Optional<UserBlocked> findByUserIdAndBlockedUserId(String userId, String blockedUserId);
}
