package com.project.foradhd.global.nativesql.repository.support;

import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.NumberExpression;
import org.locationtech.jts.geom.Point;

public interface NativeSqlSupportRepository {

    NumberExpression<Double> getDistanceSQL(Double longitudeCond, Double latitudeCond,
                                            ComparablePath<Point> pointPath);
}
