package com.project.foradhd.global.nativesql.repository.support.impl;

import com.project.foradhd.global.nativesql.repository.support.NativeSqlSupportRepository;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

@Repository
public class MysqlSupportRepository implements NativeSqlSupportRepository {

    @Override
    public NumberExpression<Double> getDistanceSQL(Double longitudeCond, Double latitudeCond, ComparablePath<Point> pointPath) {
        return Expressions.numberTemplate(Double.class,"st_distance_sphere({0}, {1})",
                Expressions.stringTemplate("point({0}, {1})",
                        longitudeCond, latitudeCond), pointPath);
    }
}
