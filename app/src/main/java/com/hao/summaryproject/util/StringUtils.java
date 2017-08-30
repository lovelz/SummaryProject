package com.hao.summaryproject.util;

import android.content.Context;
import android.support.annotation.StringRes;

import java.text.SimpleDateFormat;

/**
 * 文本工具
 * Created by liuzhu
 * on 2017/6/8.
 */

public class StringUtils {

    /**
     * 得到strings内的文字
     * @param context
     * @param resId
     * @return
     */
    public static String showResValue(Context context, @StringRes int resId){
        return context.getResources().getString(resId);
    }

    public static String formatTime(long time){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy/MM/dd");
        String formatTime = simpleDateFormat.format(time * 1000);
        return formatTime;
    }
}
