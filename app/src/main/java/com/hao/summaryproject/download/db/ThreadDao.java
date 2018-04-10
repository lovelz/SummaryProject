package com.hao.summaryproject.download.db;

import com.hao.summaryproject.download.bean.ThreadInfo;

import java.util.List;

/**
 * @author lovelz
 * @date on 2018/4/4.
 */

public interface ThreadDao {

    /**
     * 插入线程信息
     * @param threadInfo
     */
    void insertThread(ThreadInfo threadInfo);

    /**
     * 更新线程信息
     * @param threadId 线程id
     * @param url 下载路径
     * @param finished  结束大小
     */
    void updateThread(int threadId, String url, int finished);

    /**
     * 删除线程信息
     * @param url
     */
    void deleteThread(String url);

    /**
     * 获取所有的下载线程
     * @param url
     * @return
     */
    List<ThreadInfo> getAllThread(String url);

}
