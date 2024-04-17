package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    @Query("""
        select up
        from UserProfile up
        join fetch up.user
        where up.user.id = :userId
        """)
    Optional<UserProfile> findByUserIdFetch(@Param("userId") String userId);

    @Query("select up from UserProfile up where up.user.id = :userId")
    Optional<UserProfile> findByUserId(@Param("userId") String userId);

    Optional<UserProfile> findByNickname(String nickname);
}
