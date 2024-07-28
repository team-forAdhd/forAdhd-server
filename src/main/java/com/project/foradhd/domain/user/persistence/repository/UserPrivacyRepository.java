package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserPrivacyRepository extends JpaRepository<UserPrivacy, String> {

    @Query("select up from UserPrivacy up where up.user.id = :userId")
    Optional<UserPrivacy> findByUserId(@Param("userId") String userId);
}
