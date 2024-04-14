package com.project.foradhd.domain.user.persistence.repository;

import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval;
import com.project.foradhd.domain.user.persistence.entity.UserTermsApproval.UserTermsApprovalId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTermsApprovalRepository extends JpaRepository<UserTermsApproval, UserTermsApprovalId> {

    //TODO: Querydsl로 빼자
    @Query("""
        select uta from UserTermsApproval uta 
        inner join Terms t on t.id = uta.id.terms.id
        where uta.id.user.id = :userId
        order by t.seq
    """)
    List<UserTermsApproval> findByUserId(@Param("userId") String userId);
}
