package com.project.foradhd.domain.hospital.persistence.repository.impl;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalListNearbySearchCond;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalNearbyDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.QHospitalNearbyDto;
import com.project.foradhd.domain.hospital.persistence.repository.custom.HospitalRepositoryCustom;
import com.project.foradhd.domain.hospital.persistence.repository.enums.HospitalSortingOrder;
import com.project.foradhd.domain.hospital.web.enums.HospitalFilter;
import com.project.foradhd.global.nativesql.repository.support.NativeSqlSupportRepository;
import com.project.foradhd.global.paging.persistence.repository.support.QuerydslPagingSupportRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.project.foradhd.domain.hospital.persistence.entity.QHospital.hospital;
import static com.project.foradhd.domain.hospital.persistence.entity.QHospitalBookmark.hospitalBookmark;

@RequiredArgsConstructor
public class HospitalRepositoryImpl implements HospitalRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final NativeSqlSupportRepository nativeSqlSupportRepository;
    private final QuerydslPagingSupportRepository querydslPagingSupportRepository;

    @Override
    public Page<HospitalNearbyDto> findAllNearby(String userId, HospitalListNearbySearchCond searchCond, Pageable pageable) {
        HospitalSortingOrder.updateDefaultOrderExpression(getDistanceSQL(searchCond));
        OrderSpecifier<?>[] orderSpecifiers = querydslPagingSupportRepository.getOrderSpecifiers(pageable.getSort(),
                HospitalSortingOrder::valueOf);

        List<HospitalNearbyDto> content = queryFactory
                .select(new QHospitalNearbyDto(hospital,
                        getDistanceSQL(searchCond),
                        hospitalBookmark.deleted.isFalse()))
                .from(hospital)
                .leftJoin(hospitalBookmark).on(hospitalBookmark.id.hospital.id.eq(hospital.id),
                        hospitalBookmark.id.user.id.eq(userId),
                        hospitalBookmark.deleted.isFalse())
                .where(hospital.deleted.isFalse(), locationInRadius(searchCond), filtering(searchCond.getFilter()))
                .orderBy(orderSpecifiers)
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

    private BooleanExpression filtering(HospitalFilter hospitalFilter) {
        return hospitalFilter == null || hospitalFilter == HospitalFilter.ALL
                ? null : hospital.totalEvaluationReviewCount.gt(0);
    }

    private NumberExpression<Double> getDistanceSQL(HospitalListNearbySearchCond searchCond) {
        return nativeSqlSupportRepository.getDistanceSQL(searchCond.getLongitude(), searchCond.getLatitude(),
                hospital.location);
    }
}
