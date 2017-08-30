package com.hao.summaryproject.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liuzhu
 * on 2017/8/19.
 */

public class ToastUtil {

    private static Toast mToast;

    /**
     * 显示默认的toast
     * @param context
     * @param msg
     */
    public static void showMessage(Context context, String msg) {
        showLongMessage(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示自定义持续时间的toast
     * @param context
     * @param msg
     * @param duration 持续时间
     */
    public static void showLongMessage(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", duration);
        }
        mToast.setText(msg);
        mToast.show();
    }

}
