package com.project.foradhd.domain.auth.persistence.repository;

import com.project.foradhd.domain.auth.persistence.entity.AuthPassword;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthPasswordRepository extends JpaRepository<AuthPassword, String> {

    @Query("select ap from AuthPassword ap where ap.user.id = :userId")
    Optional<AuthPassword> findByUserId(@Param("userId") String userId);

    @Modifying
    @Query("delete from AuthPassword ap where ap.user.id = :userId")
    void deleteByUserId(@Param("userId") String userId);
}
