package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserPrivacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPrivacyRepository extends JpaRepository<UserPrivacy, String> {

}
