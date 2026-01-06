package com.partner.be.common;

import java.text.SimpleDateFormat;

/**
 * 定数を定義するクラス
 * Created by ${author} on 14-10-3.
 */
public interface ApiConstants {

    interface Fields {


        String SUBMIT_TOKEN_NAME = "submitToken";

        String USER_VALUE_OBJECT_KEY = "USER_VALUE_OBJECT";

    }


    interface Formats {

        String YYYYMMDD = "yyyy-MM-dd";

        String YYYYMMDD_HHMM = "yyyy-MM-dd hh:mm";

        String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss"; // update by ${author} 修改为


        SimpleDateFormat FORMAT_YYYYMMDD = new SimpleDateFormat(YYYYMMDD);

        SimpleDateFormat FORMAT_YYYYMMDD_HHMM = new SimpleDateFormat(YYYYMMDD_HHMM);

        SimpleDateFormat FORMAT_YYYYMMDD_HHMMSS = new SimpleDateFormat(YYYYMMDD_HHMMSS);
    }

    /**
     * 分隔符
     */
    interface Seperator {

        String VALUE = ",";

        String SUB_VALUE = ":";

        String AT_VALUE = "@";
    }


}
