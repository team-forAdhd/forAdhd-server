package com.project.foradhd.global.paging.persistence.repository.support;

import com.project.foradhd.domain.hospital.persistence.repository.enums.HospitalReceiptReviewSortingOrder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.ParsingUtils;
import org.springframework.stereotype.Repository;

@Repository
public class QuerydslPagingSupportRepository {

    public static final String SNAKE_CASE_DELIMITER = "_";

    public OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        if (sort.isSorted()) {
            return sort.stream().map(s -> {
                Order order = s.isAscending() ? Order.ASC : Order.DESC;
                return new OrderSpecifier<>(order, parseProperty(s.getProperty()));
            }).toArray(OrderSpecifier[]::new);
        }
        return new OrderSpecifier[]{};
    }

    private Expression<? extends Comparable<?>> parseProperty(String property) {
        String sortingOrder = ParsingUtils.reconcatenateCamelCase(property, SNAKE_CASE_DELIMITER).toUpperCase();
        return HospitalReceiptReviewSortingOrder.valueOf(sortingOrder)
                .getOrderExpression();
    }
}
