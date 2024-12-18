package com.yogosaza.oms2.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    // Asia/Seoul 시간 변경
    public static String seoulTime(LocalDateTime localDateTime) {

        ZonedDateTime seoulTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));

        // z : 타임존 정보 표시 KST(Korea Standard Time)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        String result = seoulTime.format(formatter);

        return result;
    }
}
