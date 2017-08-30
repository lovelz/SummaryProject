package com.hao.summaryproject.util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.hao.summaryproject.app.SummaryApp;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public class SystemUtil {

    /**
     * 检测是否有网络
     * this may return{@code true} when there has network or
     * return{@code false} when there has not network
     * @return
     */
    public static boolean isNetWorkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) SummaryApp.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
