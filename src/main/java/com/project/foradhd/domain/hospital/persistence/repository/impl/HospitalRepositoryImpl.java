package com.project.foradhd.domain.hospital.persistence.repository.impl;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalListNearbySearchCond;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalNearbyDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.QHospitalNearbyDto;
import com.project.foradhd.domain.hospital.persistence.repository.custom.HospitalRepositoryCustom;
import com.project.foradhd.global.nativesql.repository.support.NativeSqlSupportRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.project.foradhd.domain.hospital.persistence.entity.QDoctor.doctor;
import static com.project.foradhd.domain.hospital.persistence.entity.QHospital.hospital;
import static com.project.foradhd.domain.hospital.persistence.entity.QHospitalBookmark.hospitalBookmark;

@RequiredArgsConstructor
public class HospitalRepositoryImpl implements HospitalRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final NativeSqlSupportRepository nativeSqlSupportRepository;

    @Override
    public Page<HospitalNearbyDto> findAllNearby(String userId, HospitalListNearbySearchCond searchCond, Pageable pageable) {
        List<HospitalNearbyDto> content = queryFactory
                .select(new QHospitalNearbyDto(hospital,
                        doctor.totalGradeSum.sum().coalesce(0L),
                        doctor.totalBriefReviewCount.sum().coalesce(0),
                        doctor.totalReceiptReviewCount.sum().coalesce(0),
                        getDistanceSQL(searchCond),
                        hospitalBookmark.deleted.isFalse())
                )
                .from(hospital)
                .leftJoin(doctor).on(hospital.id.eq(doctor.hospital.id), doctor.deleted.isFalse())
                .leftJoin(hospitalBookmark).on(hospital.id.eq(hospitalBookmark.id.hospital.id), hospitalBookmark.deleted.isFalse())
                .where(hospital.deleted.isFalse(), locationInRadius(searchCond))
                .orderBy(getDistanceSQL(searchCond).asc())
                .groupBy(hospital.id)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(hospital.count())
                .from(hospital)
                .where(hospital.deleted.isFalse(), locationInRadius(searchCond));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression locationInRadius(HospitalListNearbySearchCond searchCond) {
        return searchCond.getRadius() == null ? null : getDistanceSQL(searchCond).loe(searchCond.getRadius());
    }

    private NumberExpression<Double> getDistanceSQL(HospitalListNearbySearchCond searchCond) {
        return nativeSqlSupportRepository.getDistanceSQL(searchCond.getLongitude(), searchCond.getLatitude(),
                hospital.location);
    }
}
