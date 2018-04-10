package com.hao.summaryproject.download.thread;

import com.hao.summaryproject.download.DownloadConfig;
import com.hao.summaryproject.download.bean.FileInfo;
import com.hao.summaryproject.download.bean.ThreadInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载线程
 * @author lovelz
 * @date on 2018/4/7.
 */

public class DownloadThread extends Thread {

    private static final int CONNECT_TIME_OUT = 10000;

    private FileInfo fileInfo;
    private ThreadInfo threadInfo;
    private DownloadCallback callback;

    private boolean isClose = false;
    private boolean isPause = false;

    public DownloadThread(FileInfo fileInfo, ThreadInfo threadInfo, DownloadCallback callback) {
        this.fileInfo = fileInfo;
        this.threadInfo = threadInfo;
        this.callback = callback;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        RandomAccessFile raf = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(threadInfo.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECT_TIME_OUT);
            connection.setRequestMethod("GET");
            //设置起始下载位置
            int start = threadInfo.getStart() + threadInfo.getFinished();
            connection.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());

            File downloadFile = new File(DownloadConfig.downloadPath, fileInfo.getFileName());
            raf = new RandomAccessFile(downloadFile, "rwd");
            raf.seek(start);

            //开始下载
            if (connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                inputStream = connection.getInputStream();
                byte[] bytes = new byte[1024];
                int len;
                while ((len = inputStream.read(bytes)) != -1) {
                    raf.read(bytes, 0, len);
                    //下载进度回调
                    callback.downloadProgress(len);
                    //保存下载进度
                    threadInfo.setFinished(threadInfo.getFinished() + len);

                    if (isClose) {
                        //取消下载回调
                        callback.downloadCancel(threadInfo);
                        return;
                    }

                    if (isPause) {
                        //暂停下载回调
                        callback.downloadPause(threadInfo);
                        return;
                    }
                }

                //下载完成
                callback.downloadComplete(threadInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (raf != null) {
                    raf.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
}
