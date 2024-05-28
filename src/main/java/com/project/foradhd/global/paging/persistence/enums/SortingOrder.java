package com.project.foradhd.global.paging.persistence.enums;

import com.querydsl.core.types.Expression;

public interface SortingOrder {

    Expression<? extends Comparable<?>> getOrderExpression();
}
