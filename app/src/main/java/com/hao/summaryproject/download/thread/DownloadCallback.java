package com.hao.summaryproject.download.thread;

import com.hao.summaryproject.download.bean.ThreadInfo;

/**
 * @author lovelz
 * @date on 2018/4/4.
 */

public interface DownloadCallback {

    /**
     * 下载暂停回调
     * @param threadInfo
     */
    void downloadPause(ThreadInfo threadInfo);

    /**
     * 下载取消回调
     * @param threadInfo
     */
    void downloadCancel(ThreadInfo threadInfo);

    /**
     * 下载进度回调
     * @param length
     */
    void downloadProgress(int length);

    /**
     * 下载完成回调
     * @param threadInfo
     */
    void downloadComplete(ThreadInfo threadInfo);

}
