package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    @Query("""
        select up
        from UserProfile up
        join fetch up.user
        """)
    Optional<UserProfile> findByUserIdFetch(String userId);

    Optional<UserProfile> findByNickname(String nickname);
}
