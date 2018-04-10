package com.hao.summaryproject.download.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.hao.summaryproject.R;
import com.hao.summaryproject.download.DownloadConfig;
import com.hao.summaryproject.download.bean.FileInfo;
import com.hao.summaryproject.download.thread.DownloadTask;
import com.hao.summaryproject.download.thread.InitThread;
import com.hao.summaryproject.util.ToastUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载更新服务
 *
 * @author lovelz
 * @date on 2018/4/4.
 */

public class UpdateService extends Service {

    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_PAUSE = "ACTION_PAUSE";

    private DownloadBroadcastReceiver downloadReceiver;

    //是否正在下载中
    private boolean isDownloading = false;
    //网络状态
    private boolean netStates;
    //是否手动暂停
    private boolean isUserPause;
    //下载文件信息
    private FileInfo curFileInfo;

    public static ExecutorService executorService = Executors.newCachedThreadPool();

    //下载任务集合
    private List<DownloadTask> downloadTaskList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        downloadReceiver = new DownloadBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadConfig.ACTION_START);
        intentFilter.addAction(DownloadConfig.ACTION_REFRESH);
        intentFilter.addAction(DownloadConfig.ACTION_PAUSE);
        intentFilter.addAction(DownloadConfig.ACTION_FINISHED);
        intentFilter.addAction(DownloadConfig.ACTION_CANCEL);
        intentFilter.addAction(DownloadConfig.ACTION_ERROR);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //注册
        registerReceiver(downloadReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (TextUtils.equals(intent.getAction(), ACTION_START)) {
                isDownloading = false;
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("download_file_info");

                for (DownloadTask downloadTask : downloadTaskList) {
                    //如果下载任务中存在该文件的下载任务，则直接返回
                    if (TextUtils.equals(downloadTask.getFileInfo().getUrl(), fileInfo.getUrl())) {
                        return super.onStartCommand(intent, flags, startId);
                    }
                }

                executorService.execute(new InitThread(getBaseContext(), fileInfo));
            } else if (TextUtils.equals(intent.getAction(), ACTION_PAUSE)) {
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("download_file_info");
                DownloadTask pauseTask = null;
                for (DownloadTask downloadTask : downloadTaskList) {
                    if (TextUtils.equals(downloadTask.getFileInfo().getUrl(), fileInfo.getUrl())) {
                        downloadTask.pauseDownload();
                        pauseTask = downloadTask;
                        break;
                    }
                }
                //移除下载任务
                downloadTaskList.remove(pauseTask);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 下载广播 监听
     */
    private class DownloadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            FileInfo fileInfo;
            int progress;
            switch (action) {
                //开始下载
                case DownloadConfig.ACTION_START:
                    isDownloading = true;
                    fileInfo = (FileInfo) intent.getSerializableExtra("download_file_info");
                    //开始下载咯
                    DownloadTask downloadTask = new DownloadTask(UpdateService.this, fileInfo, 3);
                    downloadTaskList.add(downloadTask);

                    if (nfManager == null) {
                        sendDefaultNotification();
                    }
                    break;
                //下载刷新
                case DownloadConfig.ACTION_REFRESH:
                    fileInfo = (FileInfo) intent.getSerializableExtra("download_file_info");
                    int speed = intent.getIntExtra("download_speed", 0);

                    progress = (int) (fileInfo.getFinished() * 1.0f / fileInfo.getLength() * 1.0f * 100);
                    DecimalFormat df = new DecimalFormat("#.##");
                    String speedInfo = df.format(speed * 1.0 / 1024);
                    updateNotification(fileInfo.getLength(), progress, speedInfo);
                    break;
                //暂停下载
                case DownloadConfig.ACTION_PAUSE:
                    isDownloading = false;
                    fileInfo = (FileInfo) intent.getSerializableExtra("download_file_info");
                    progress = (int) (fileInfo.getFinished() * 1.0f / fileInfo.getLength() * 1.0f * 100);
                    updateNotification(fileInfo.getLength(), progress, "0");
                    break;
                //下载结束
                case DownloadConfig.ACTION_FINISHED:
                    fileInfo = (FileInfo) intent.getSerializableExtra("download_file_info");
                    DownloadTask finishTask = null;
                    for (DownloadTask task : downloadTaskList) {
                        if (TextUtils.equals(fileInfo.getUrl(), task.getFileInfo().getUrl())) {
                            finishTask = task;
                            break;
                        }
                    }
                    if (finishTask != null) {
                        downloadTaskList.remove(finishTask);
                    }
                    cancelNotification();
                    curFileInfo = null;
                    installApk(fileInfo);
                    break;
                //取消下载
                case DownloadConfig.ACTION_CANCEL:
                    fileInfo = (FileInfo) intent.getSerializableExtra("download_file_info");
                    File file = new File(DownloadConfig.downloadPath, fileInfo.getFileName());
                    if (file.exists()) {
                        file.delete();
                    }
                    cancelNotification();
                    break;
                //下载错误
                case DownloadConfig.ACTION_ERROR:
                    ToastUtil.showMessage(context, intent.getStringExtra("error_info"));
                    break;
                //手动下载暂停或开始
                case BUTTON_ACTION:
                    if (!netStates) {
                        ToastUtil.showMessage(context, "请先检查网络");
                        return;
                    }
                    //根据是否正在下载判断是暂停还是开始
                    if (isDownloading) {
                        isUserPause = true;
                        Intent pauseIntent = new Intent(context, UpdateService.class);
                        pauseIntent.setAction(ACTION_PAUSE);
                        pauseIntent.putExtra("download_file_info", curFileInfo);
                        startService(pauseIntent);
                    } else {
                        isUserPause = false;
                        Intent startIntent = new Intent(context, UpdateService.class);
                        startIntent.setAction(ACTION_START);
                        startIntent.putExtra("download_file_info", curFileInfo);
                        startService(startIntent);
                    }
                    break;
                //手动下载取消
                case BUTTON_CLOSE_ACTION:
                    if (curFileInfo != null) {
                        DownloadTask cancelTask = null;
                        for (DownloadTask task : downloadTaskList) {
                            if (TextUtils.equals(task.getFileInfo().getUrl(), curFileInfo.getUrl())) {
                                task.closeDownload();
                                cancelTask = task;
                                break;
                            }
                        }

                        if (cancelTask != null) {
                            downloadTaskList.remove(cancelTask);
                        } else {
                            DownloadTask curTask = new DownloadTask(UpdateService.this, curFileInfo, 3);
                            curTask.closeDownload();
                        }
                    }
                    break;
                //网络变化状况
                default:
                    if (curFileInfo == null) return;
                    ConnectivityManager ctManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    //手机网络信息
                    NetworkInfo mobileNetInfo = ctManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    //WiFi信息
                    NetworkInfo wifiNetInfo = ctManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (!mobileNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                        //网络已断开
                        netStates = false;
                        if (!isDownloading) return;

                        Intent netIntent = new Intent(context, UpdateService.class);
                        netIntent.setAction(ACTION_PAUSE);
                        netIntent.putExtra("download_file_info", curFileInfo);
                        startService(netIntent);
                    } else {
                        //网络已连接
                        netStates = true;
                        if (isDownloading || isUserPause) return;

                        Intent netIntent = new Intent(context, UpdateService.class);
                        netIntent.setAction(ACTION_START);
                        netIntent.putExtra("download_file_info", curFileInfo);
                        startService(netIntent);
                    }
                    break;
            }
        }
    }

    private NotificationManager nfManager;
    private RemoteViews remoteView;
    private final String BUTTON_ACTION = "BUTTON_ACTION";
    private final String BUTTON_CLOSE_ACTION = "BUTTON_CLOSE_ACTION";
    private static final String channel_id = "version_update";
    private static final String channel_name = "版本更新";
    private NotificationCompat.Builder nfBuilder;
    private Notification notification;
    private final int notificationId = 100001;

    /**
     * 发送通知
     */
    private void sendDefaultNotification() {
        remoteView = new RemoteViews(getPackageName(), R.layout.layout_notifi);
        //设置通知标题
        remoteView.setTextViewText(R.id.textView, TextUtils.isEmpty(DownloadConfig.notifyTitle) ?
                getString(R.string.app_name) : DownloadConfig.notifyTitle);
        //设置通知图标
        remoteView.setImageViewResource(R.id.icon, DownloadConfig.notifyIconResId == 0 ?
                R.mipmap.ic_launcher : DownloadConfig.notifyIconResId);
        //暂停与开始点击事件
        Intent buttonAction = new Intent(BUTTON_ACTION);
        PendingIntent buttonIntent = PendingIntent.getBroadcast(this, 1, buttonAction, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.btn1, buttonIntent);
        //关闭点击事件
        Intent closeAction = new Intent(BUTTON_CLOSE_ACTION);
        PendingIntent closeIntent = PendingIntent.getBroadcast(this, 1, closeAction, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.btnClose, closeIntent);

        nfManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Android 8.0开始通知需要添加channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nfChannel = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
            nfManager.createNotificationChannel(nfChannel);
            nfBuilder = new NotificationCompat.Builder(this, channel_id);
        } else {
            nfBuilder = new NotificationCompat.Builder(this);
        }

        //notification相关配置
        nfBuilder.setTicker("开始下载");
        nfBuilder.setContent(remoteView);
        nfBuilder.setAutoCancel(true);
        nfBuilder.setOngoing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            nfBuilder.setPriority(Notification.PRIORITY_MAX);
        }
        //通知
        notification = nfBuilder.build();
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        nfManager.notify(notificationId, notification);
    }

    /**
     * 实时更新通知
     * @param length 安装包大小
     * @param progress 下载进度
     * @param speedInfo 下载速度
     */
    private void updateNotification(int length, int progress, String speedInfo) {
        remoteView.setImageViewResource(R.id.btn1, isDownloading ? R.mipmap.ic_pause : R.mipmap.ic_continue);
        remoteView.setProgressBar(R.id.progressBar, 100, progress, false);

        DecimalFormat df = new DecimalFormat("#.##");
        remoteView.setTextViewText(R.id.textSize, df.format(b2mbDouble(length)) + "");
        remoteView.setTextViewText(R.id.textSpeed, speedInfo);
        //刷新
        nfManager.notify(notificationId, notification);
    }

    /**
     * 取消通知
     */
    private void cancelNotification() {
        nfManager.cancel(notificationId);
    }

    /**
     * byte转mb
     * @param byteValue
     * @return
     */
    private double b2mbDouble(long byteValue) {
        return byteValue * 1.0 / 1024 / 1024;
    }

    /**
     * 自动安装apk包
     * @param fileInfo
     */
    private void installApk(FileInfo fileInfo) {
        String filePath = DownloadConfig.downloadPath + "/" + fileInfo.getFileName();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new NullPointerException("未发现下载文件");
        }
        //调用系统安装环境
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        //Android 7.0后文件读写发生变化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.hao.summaryproject.provider", file);
            installIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        //进入安装界面
        getApplication().startActivity(installIntent);
        collapseStatusBar();
    }

    /**
     * 收起通知栏
     */
    public void collapseStatusBar() {
        try {
            Object statusBarManager = getSystemService("statusbar");
            Method collapse;
            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadReceiver);
    }
}
