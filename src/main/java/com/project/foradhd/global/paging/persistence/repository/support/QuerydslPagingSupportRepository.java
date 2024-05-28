package com.project.foradhd.global.paging.persistence.repository.support;

import com.project.foradhd.global.paging.persistence.enums.SortingOrder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.ParsingUtils;
import org.springframework.stereotype.Repository;

import java.util.function.Function;

@Repository
public class QuerydslPagingSupportRepository {

    public static final String SNAKE_CASE_DELIMITER = "_";

    public OrderSpecifier<?>[] getOrderSpecifiers(Sort sort, Function<String, SortingOrder> sortingOrderFunction) {
        if (sort.isSorted()) {
            return sort.stream().map(s -> {
                Order order = s.isAscending() ? Order.ASC : Order.DESC;
                return new OrderSpecifier<>(order, parseProperty(s.getProperty(), sortingOrderFunction));
            }).toArray(OrderSpecifier[]::new);
        }
        return new OrderSpecifier[]{};
    }

    private Expression<? extends Comparable<?>> parseProperty(String property,
                                                            Function<String, SortingOrder> sortingOrderFunction) {
        String sortingOrderName = ParsingUtils.reconcatenateCamelCase(property, SNAKE_CASE_DELIMITER).toUpperCase();
        SortingOrder sortingOrder = sortingOrderFunction.apply(sortingOrderName);
        return sortingOrder.getOrderExpression();
    }
}
