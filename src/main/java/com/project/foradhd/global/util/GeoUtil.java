package com.project.foradhd.global.util;

public abstract class GeoUtil {

    // 지구의 반지름(m 단위)
    private static final double EARTH_RADIUS = 6371000;

    public static double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        //위도와 경도를 라디안으로 변환
        double latitudeDistance = Math.toRadians(latitude2 - latitude1);
        double longitudeDistance = Math.toRadians(longitude2 - longitude1);

        //해버사인 공식 사용
        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        //지구의 반지름을 곱해 최종 거리 반환 (m 단위)
        return EARTH_RADIUS * c;
    }
}
