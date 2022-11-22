package com.mszlu.courseware.utils;

import cn.hutool.core.date.DateTime;

public class IdUtil {

    public static String getOrderId(Long count) {
        String id1 = new DateTime().toString("yyyyMMdd");
        String id2 = getFourId(count);
        return id1 + id2;
    }

    private static String getFourId(Long count) {
        String id2 = "";
        if (count < 10 && count >= 0) {
            id2 = "000" + (count+1);
        } else if (count >= 10 && count < 100) {
            id2 = "00" + (count+1);
        } else if (count >= 100 && count < 1000) {
            id2 = "0" + (count+1);
        } else if (count >= 1000) {
            id2 = "" + (count+1);
        }
        return id2;
    }
}
