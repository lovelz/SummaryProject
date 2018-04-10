package com.hao.summaryproject.download;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.IdRes;

/**
 * @author lovelz
 * @date on 2018/4/4.
 */

public class DownloadConfig {

    /**
     * 通知栏标题
     */
    public static String notifyTitle;

    /**
     * 通知栏图标
     */
    public static int notifyIconResId = 0;

    /**
     * 下载路径
     */
    public static String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";

    /**
     * 开始下载
     */
    public final static String ACTION_START = "ACTION_START";

    /**
     * 下载进度刷新
     */
    public final static String ACTION_REFRESH = "ACTION_REFRESH";

    /**
     * 暂停下载
     */
    public final static String ACTION_PAUSE = "ACTION_PAUSE";

    /**
     * 下载完成
     */
    public final static String ACTION_FINISHED = "ACTION_FINISHED";

    /**
     * 取消下载
     */
    public final static String ACTION_CANCEL = "ACTION_CANCEL";

    /**
     * 下载错误
     */
    public final static String ACTION_ERROR = "ACTION_ERROR";

    private static DownloadConfig downloadConfig = new DownloadConfig();
    private Context mContext;

    private DownloadConfig() {

    }

    public static DownloadConfig getInstance() {
        return downloadConfig;
    }

    /**
     * 设置上下文
     * @param context
     * @return
     */
    public DownloadConfig setContext(Context context) {
        this.mContext = context;
        return downloadConfig;
    }

    /**
     * 设置文件下载路径
     * @param path
     * @return
     */
    public DownloadConfig setFileSavePath(String path) {
        downloadPath = path;
        return downloadConfig;
    }

    /**
     * 设置通知标题
     * @param title
     * @return
     */
    public DownloadConfig setNotifyTitle(String title) {
        notifyTitle = title;
        return downloadConfig;
    }

    /**
     * 设置通知图标
     * @param resId
     * @return
     */
    public DownloadConfig setNotifyIconResId(@IdRes int resId) {
        notifyIconResId = resId;
        return downloadConfig;
    }
}
