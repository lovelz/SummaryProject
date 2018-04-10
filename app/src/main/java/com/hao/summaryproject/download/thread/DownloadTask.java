package com.hao.summaryproject.download.thread;

import android.content.Context;
import android.content.Intent;

import com.hao.summaryproject.download.DownloadConfig;
import com.hao.summaryproject.download.bean.FileInfo;
import com.hao.summaryproject.download.bean.ThreadInfo;
import com.hao.summaryproject.download.db.ThreadDao;
import com.hao.summaryproject.download.db.ThreadDaoImpl;
import com.hao.summaryproject.download.service.UpdateService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lovelz
 * @date on 2018/4/4.
 */

public class DownloadTask implements DownloadCallback {

    private Context mContext;
    private FileInfo fileInfo;
    private int downThreadCount = 3;
    private ThreadDao threadDao;

    //所有线程信息
    private List<ThreadInfo> threadInfoList;

    //下载线程集合
    private List<DownloadThread> downloadThreadList = new ArrayList<DownloadThread>();

    //记录总下载进度
    private int finishedProgress = 0;

    public DownloadTask(Context context, FileInfo fileInfo, int downThreadCount) {
        this.mContext = context;
        this.fileInfo = fileInfo;
        this.downThreadCount = downThreadCount;
        this.threadDao = new ThreadDaoImpl(context);

        initThreadInfo();
    }

    /**
     * 初始化线程相关信息
     */
    private void initThreadInfo() {
        //获取所有已存在的下载线程相关信息
        threadInfoList = threadDao.getAllThread(fileInfo.getUrl());
        if (threadInfoList.size() == 0) {
            //说明还没有存在下载线程信息，为第一次下载哦

            //平分下载大小
            int length = fileInfo.getLength() / downThreadCount;
            for (int i = 0; i < downThreadCount; i++) {
                ThreadInfo threadInfo = new ThreadInfo(i, i * length, (i + 1) * length - 1,
                        0, fileInfo.getUrl());
                if (i == (downThreadCount - 1)) {
                    threadInfo.setEnd(fileInfo.getLength());
                }
                //将线程信息保存至数据库中
                threadDao.insertThread(threadInfo);
                threadInfoList.add(threadInfo);
            }
        }

        //创建下载线程开始下载咯
        for (ThreadInfo threadInfo : threadInfoList) {
            finishedProgress += threadInfo.getFinished();
            DownloadThread downloadThread = new DownloadThread(fileInfo, threadInfo, this);
            UpdateService.executorService.execute(downloadThread);
            downloadThreadList.add(downloadThread);
        }
    }

    /**
     * 暂停下载任务
     */
    public void pauseDownload() {
        for (DownloadThread downloadThread : downloadThreadList) {
            if (downloadThread != null) {
                downloadThread.setPause(true);
            }
        }
    }

    /**
     * 取消下载任务
     */
    public void closeDownload() {
        for (DownloadThread downloadThread : downloadThreadList) {
            if (downloadThread != null) {
                downloadThread.setClose(true);
            }
        }
    }

    @Override
    public void downloadPause(ThreadInfo threadInfo) {
        threadDao.updateThread(threadInfo.getId(), threadInfo.getUrl(), threadInfo.getFinished());
        Intent intent = new Intent(DownloadConfig.ACTION_PAUSE);
        intent.putExtra("download_file_info", fileInfo);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void downloadCancel(ThreadInfo threadInfo) {
        threadDao.deleteThread(threadInfo.getUrl());
        Intent intent = new Intent(DownloadConfig.ACTION_CANCEL);
        intent.putExtra("download_file_info", fileInfo);
        mContext.sendBroadcast(intent);
    }

    private long curTime = 0;
    private int speed = 0;
    @Override
    public void downloadProgress(int length) {
        finishedProgress += length;
        speed += length;
        long diffTime = System.currentTimeMillis() - curTime;
        //每隔500毫秒或者下载完成刷新进度
        if (diffTime > 500 || (finishedProgress == fileInfo.getLength())) {
            fileInfo.setFinished(finishedProgress);
            speed = (int) (speed / (diffTime * 0.001));
            Intent intent = new Intent(DownloadConfig.ACTION_REFRESH);
            intent.putExtra("download_speed", speed);
            intent.putExtra("download_file_info", fileInfo);
            mContext.sendBroadcast(intent);

            speed = 0;
            curTime = System.currentTimeMillis();
        }
    }

    @Override
    public void downloadComplete(ThreadInfo threadInfo) {
        for (ThreadInfo info : threadInfoList) {
            if (info.getId() == threadInfo.getId()) {
                //移除已下载完成的
                threadInfoList.remove(info);
                break;
            }
        }

        if (threadInfoList.size() == 0) {
            //表示已下载完成
            threadDao.deleteThread(threadInfo.getUrl());
            Intent intent = new Intent(DownloadConfig.ACTION_FINISHED);
            intent.putExtra("download_file_info", fileInfo);
            mContext.sendBroadcast(intent);
        }
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }
}
