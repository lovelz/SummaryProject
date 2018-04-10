package com.hao.summaryproject.download.thread;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hao.summaryproject.download.DownloadConfig;
import com.hao.summaryproject.download.bean.FileInfo;
import com.hao.summaryproject.download.utils.MD5Util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author lovelz
 * @date on 2018/4/8.
 */

public class InitThread extends Thread {

    private Context mContext;
    private FileInfo fileInfo;

    public InitThread(Context context, FileInfo fileInfo) {
        this.mContext = context;
        this.fileInfo = fileInfo;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        RandomAccessFile raf = null;
        try {
            URL url = new URL(fileInfo.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");

            int fileLength = -1;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                fileLength = connection.getContentLength();
            }

            //检验文件大小，如果小于0，表示此文件有问题或者网络有问题
            if (fileLength <= 0) {
                Intent intent = new Intent(DownloadConfig.ACTION_ERROR);
                intent.putExtra("error_info", "下载文件存在问题");
                mContext.sendBroadcast(intent);
                return;
            }

            //判断文件是否下载过
            File downloadFile = new File(DownloadConfig.downloadPath, fileInfo.getFileName());
            if (downloadFile.exists()) {
                String fileVision = MD5Util.getVersionNameFromApk(mContext, downloadFile.getPath());
                if (!TextUtils.isEmpty(fileVision) && !TextUtils.isEmpty(fileInfo.getVersion()) &&
                        TextUtils.equals(fileVision, fileInfo.getVersion())) {
                    Intent intent = new Intent(DownloadConfig.ACTION_FINISHED);
                    intent.putExtra("download_file_info", fileInfo);
                    mContext.sendBroadcast(intent);
                    return;
                }
            }

            //创建文件以及读取文件大小相关操作
            File downloadDir = new File(DownloadConfig.downloadPath);
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }

            File apkFile = new File(downloadDir, fileInfo.getFileName());
            raf = new RandomAccessFile(apkFile, "rwd");
            raf.setLength(fileLength);
            fileInfo.setLength(fileLength);

            Intent intent = new Intent(DownloadConfig.ACTION_START);
            intent.putExtra("download_file_info", fileInfo);
            mContext.sendBroadcast(intent);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
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
}
