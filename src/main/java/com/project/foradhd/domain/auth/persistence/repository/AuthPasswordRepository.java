package com.project.foradhd.domain.auth.persistence.repository;

import com.project.foradhd.domain.auth.persistence.entity.AuthPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthPasswordRepository extends JpaRepository<AuthPassword, String> {

}
