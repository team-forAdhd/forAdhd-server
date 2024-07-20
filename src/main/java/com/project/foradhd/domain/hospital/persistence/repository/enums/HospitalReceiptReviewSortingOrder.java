package com.project.foradhd.domain.hospital.persistence.repository.enums;

import com.project.foradhd.global.paging.persistence.enums.SortingOrder;
import com.querydsl.core.types.Expression;

import static com.project.foradhd.domain.hospital.persistence.entity.QHospitalReceiptReview.hospitalReceiptReview;

public enum HospitalReceiptReviewSortingOrder implements SortingOrder {

    CREATED_AT {
        @Override
        public Expression<? extends Comparable<?>> getOrderExpression() {
            return hospitalReceiptReview.createdAt;
        }
    },
    HELP_COUNT {
        @Override
        public Expression<? extends Comparable<?>> getOrderExpression() {
            return hospitalReceiptReview.helpCount;
        }
    }
}
