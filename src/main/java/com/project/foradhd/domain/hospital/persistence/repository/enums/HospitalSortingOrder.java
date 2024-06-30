package com.project.foradhd.domain.hospital.persistence.repository.enums;

import com.project.foradhd.global.paging.persistence.enums.SortingOrder;
import com.querydsl.core.types.Expression;

import static com.project.foradhd.domain.hospital.persistence.entity.QHospital.hospital;

public enum HospitalSortingOrder implements SortingOrder {

    DISTANCE {
        @Override
        public Expression<? extends Comparable<?>> getOrderExpression() {
            return HospitalSortingOrder.defaultOrderExpression;
        }
    },
    REVIEW_COUNT {
        @Override
        public Expression<? extends Comparable<?>> getOrderExpression() {
            return hospital.totalReceiptReviewCount;
        }
    };

    private static Expression<? extends Comparable<?>> defaultOrderExpression;

    public static void updateDefaultOrderExpression(Expression<? extends Comparable<?>> defaultOrderExpression) {
        HospitalSortingOrder.defaultOrderExpression = defaultOrderExpression;
    }
}
