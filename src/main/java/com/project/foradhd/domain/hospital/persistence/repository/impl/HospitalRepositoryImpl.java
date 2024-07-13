package com.project.foradhd.domain.hospital.persistence.repository.impl;

import com.project.foradhd.domain.hospital.business.dto.in.HospitalListNearbySearchCond;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalBookmarkDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.HospitalNearbyDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.QHospitalBookmarkDto;
import com.project.foradhd.domain.hospital.persistence.dto.out.QHospitalNearbyDto;
import com.project.foradhd.domain.hospital.persistence.repository.custom.HospitalRepositoryCustom;
import com.project.foradhd.domain.hospital.persistence.repository.enums.HospitalBookmarkSoringOrder;
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
        Double longitude = searchCond.getLongitude();
        Double latitude = searchCond.getLatitude();
        Integer radius = searchCond.getRadius();
        HospitalSortingOrder.updateDefaultOrderExpression(getDistanceSQL(longitude, latitude));
        OrderSpecifier<?>[] orderSpecifiers = querydslPagingSupportRepository.getOrderSpecifiers(pageable.getSort(),
                HospitalSortingOrder::valueOf);

        List<HospitalNearbyDto> content = queryFactory
                .select(new QHospitalNearbyDto(hospital,
                        getDistanceSQL(longitude, latitude),
                        hospitalBookmark.deleted.isFalse()))
                .from(hospital)
                .leftJoin(hospitalBookmark).on(hospitalBookmark.id.hospital.id.eq(hospital.id),
                        hospitalBookmark.id.user.id.eq(userId),
                        hospitalBookmark.deleted.isFalse())
                .where(hospital.deleted.isFalse(), locationInRadius(longitude, latitude, radius), filtering(searchCond.getFilter()))
                .orderBy(orderSpecifiers)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(hospital.count())
                .from(hospital)
                .where(hospital.deleted.isFalse(), locationInRadius(longitude, latitude, radius));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<HospitalBookmarkDto> findAllBookmark(String userId, Pageable pageable) {
        OrderSpecifier<?>[] orderSpecifiers = querydslPagingSupportRepository.getOrderSpecifiers(pageable.getSort(),
                HospitalBookmarkSoringOrder::valueOf);

        List<HospitalBookmarkDto> content = queryFactory.select(new QHospitalBookmarkDto(hospital))
                .from(hospital)
                .innerJoin(hospitalBookmark).on(hospitalBookmark.id.hospital.id.eq(hospital.id),
                        hospitalBookmark.id.user.id.eq(userId),
                        hospitalBookmark.deleted.isFalse())
                .where(hospital.deleted.isFalse())
                .orderBy(orderSpecifiers)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(hospital.count())
                .from(hospital)
                .innerJoin(hospitalBookmark).on(hospitalBookmark.id.hospital.id.eq(hospital.id),
                        hospitalBookmark.id.user.id.eq(userId),
                        hospitalBookmark.deleted.isFalse())
                .where(hospital.deleted.isFalse());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression locationInRadius(Double longitude, Double latitude, Integer radius) {
        return radius == null ? null : getDistanceSQL(longitude, latitude).loe(radius);
    }

    private BooleanExpression filtering(HospitalFilter hospitalFilter) {
        if (hospitalFilter == null || hospitalFilter == HospitalFilter.ALL) return null;
        if (hospitalFilter == HospitalFilter.EVALUATION_REVIEW) return hospital.totalEvaluationReviewCount.gt(0);
        return null;
    }

    private NumberExpression<Double> getDistanceSQL(Double longitude, Double latitude) {
        return nativeSqlSupportRepository.getDistanceSQL(longitude, latitude, hospital.location);
    }
}
