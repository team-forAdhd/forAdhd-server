package com.project.foradhd.domain.hospital.persistence.repository.impl;

import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalReceiptReviewDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.QHospitalReceiptReviewDto;
import com.project.foradhd.domain.hospital.persistence.repository.custom.HospitalReceiptReviewRepositoryCustom;
import com.project.foradhd.global.paging.persistence.repository.support.QuerydslPagingSupportRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.project.foradhd.domain.hospital.persistence.entity.QDoctor.doctor;
import static com.project.foradhd.domain.hospital.persistence.entity.QHospital.hospital;
import static com.project.foradhd.domain.hospital.persistence.entity.QHospitalReceiptReview.hospitalReceiptReview;
import static com.project.foradhd.domain.hospital.persistence.entity.QHospitalReceiptReviewHelp.hospitalReceiptReviewHelp;
import static com.project.foradhd.domain.user.persistence.entity.QUserProfile.userProfile;

@RequiredArgsConstructor
public class HospitalReceiptReviewRepositoryImpl implements HospitalReceiptReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QuerydslPagingSupportRepository querydslPagingSupportRepository;

    @Override
    public Page<HospitalReceiptReviewDto> findAll(String userId, String hospitalId, String doctorId, Pageable pageable) {
        OrderSpecifier<?>[] orderSpecifiers = querydslPagingSupportRepository.getOrderSpecifiers(pageable.getSort());
        List<HospitalReceiptReviewDto> content = queryFactory
                .select(new QHospitalReceiptReviewDto(
                        hospitalReceiptReview, userProfile,
                        hospitalReceiptReviewHelp.isNotNull(),
                        new CaseBuilder()
                                .when(userProfile.user.id.eq(userId)).then(true)
                                .otherwise(false)
                ))
                .from(hospitalReceiptReview)
                .innerJoin(hospitalReceiptReview.doctor, doctor).on(doctor.id.eq(doctorId))
                .innerJoin(doctor.hospital, hospital).on(hospital.id.eq(hospitalId))
                .innerJoin(userProfile).on(hospitalReceiptReview.user.id.eq(userProfile.user.id))
                .leftJoin(hospitalReceiptReviewHelp)
                .on(hospitalReceiptReview.id.eq(hospitalReceiptReviewHelp.id.hospitalReceiptReview.id),
                        hospitalReceiptReviewHelp.id.user.id.eq(userId),
                        hospitalReceiptReviewHelp.deleted.isFalse())
                .orderBy(orderSpecifiers)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(hospitalReceiptReview.count())
                .from(hospitalReceiptReview)
                .innerJoin(hospitalReceiptReview.doctor, doctor).on(doctor.id.eq(doctorId))
                .innerJoin(doctor.hospital, hospital).on(hospital.id.eq(hospitalId))
                .innerJoin(userProfile).on(hospitalReceiptReview.user.id.eq(userProfile.user.id));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
