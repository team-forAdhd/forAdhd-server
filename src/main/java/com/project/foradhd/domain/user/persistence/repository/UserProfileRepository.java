package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

}
