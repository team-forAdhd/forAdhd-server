package com.project.foradhd.domain.hospital.persistence.repository.enums;

import com.project.foradhd.global.paging.persistence.enums.SortingOrder;
import com.querydsl.core.types.Expression;

import static com.project.foradhd.domain.hospital.persistence.entity.QHospitalBookmark.hospitalBookmark;

public enum HospitalBookmarkSoringOrder implements SortingOrder {

    DISTANCE {
        @Override
        public Expression<? extends Comparable<?>> getOrderExpression() {
            return HospitalBookmarkSoringOrder.defaultOrderExpression;
        }
    },
    CREATED_AT {
        @Override
        public Expression<? extends Comparable<?>> getOrderExpression() {
            return hospitalBookmark.createdAt;
        }
    };

    private static Expression<? extends Comparable<?>> defaultOrderExpression;

    public static void updateDefaultOrderExpression(Expression<? extends Comparable<?>> defaultOrderExpression) {
        HospitalBookmarkSoringOrder.defaultOrderExpression = defaultOrderExpression;
    }
}
