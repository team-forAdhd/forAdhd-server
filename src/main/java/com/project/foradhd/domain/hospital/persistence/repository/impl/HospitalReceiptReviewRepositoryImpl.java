package com.project.foradhd.domain.hospital.persistence.repository.impl;

import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalReceiptReviewDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.QHospitalReceiptReviewDto;
import com.project.foradhd.domain.hospital.persistence.repository.custom.HospitalReceiptReviewRepositoryCustom;
import com.project.foradhd.domain.hospital.persistence.repository.enums.HospitalReceiptReviewSortingOrder;
import com.project.foradhd.global.paging.persistence.repository.support.QuerydslPagingSupportRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.project.foradhd.domain.hospital.persistence.entity.QDoctor.doctor;
import static com.project.foradhd.domain.hospital.persistence.entity.QHospitalReceiptReview.hospitalReceiptReview;
import static com.project.foradhd.domain.hospital.persistence.entity.QHospitalReceiptReviewHelp.hospitalReceiptReviewHelp;
import static com.project.foradhd.domain.user.persistence.entity.QUserProfile.userProfile;

@RequiredArgsConstructor
public class HospitalReceiptReviewRepositoryImpl implements HospitalReceiptReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QuerydslPagingSupportRepository querydslPagingSupportRepository;

    @Override
    public Page<HospitalReceiptReviewDto> findAll(String userId, String hospitalId, String doctorId, Pageable pageable) {
        OrderSpecifier<?>[] orderSpecifiers = querydslPagingSupportRepository.getOrderSpecifiers(pageable.getSort(),
                HospitalReceiptReviewSortingOrder::valueOf);
        List<HospitalReceiptReviewDto> content = queryFactory
                .select(new QHospitalReceiptReviewDto(
                        hospitalReceiptReview, userProfile, doctor,
                        hospitalReceiptReviewHelp.isNotNull(),
                        new CaseBuilder()
                                .when(hospitalReceiptReview.user.id.eq(userId)).then(true)
                                .otherwise(false)))
                .from(hospitalReceiptReview)
                .innerJoin(userProfile).on(userProfile.user.id.eq(hospitalReceiptReview.user.id))
                .leftJoin(hospitalReceiptReview.doctor, doctor)
                .leftJoin(hospitalReceiptReviewHelp)
                .on(hospitalReceiptReviewHelp.id.hospitalReceiptReview.id.eq(hospitalReceiptReview.id),
                        hospitalReceiptReviewHelp.id.user.id.eq(userId),
                        hospitalReceiptReviewHelp.deleted.isFalse())
                .where(hospitalReceiptReview.hospital.id.eq(hospitalId), doctorIdEq(doctorId),
                        hospitalReceiptReview.deleted.isFalse())
                .orderBy(orderSpecifiers)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(hospitalReceiptReview.count())
                .from(hospitalReceiptReview)
                .innerJoin(userProfile).on(userProfile.user.id.eq(hospitalReceiptReview.user.id))
                .leftJoin(hospitalReceiptReview.doctor, doctor)
                .where(hospitalReceiptReview.hospital.id.eq(hospitalId), doctorIdEq(doctorId),
                        hospitalReceiptReview.deleted.isFalse());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private Predicate doctorIdEq(String doctorId) {
        return doctorId == null ? null : doctor.id.eq(doctorId);
    }
}
