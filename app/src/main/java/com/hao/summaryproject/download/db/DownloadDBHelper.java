package com.hao.summaryproject.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author lovelz
 * @date on 2018/4/4.
 */

public class DownloadDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "version_download";

    private static DownloadDBHelper downloadDBHelper = null;

    /**
     * 获取DownloadDBHelper对象
     * @param context
     * @return
     */
    public static DownloadDBHelper getInstance(Context context) {
        if (downloadDBHelper == null) {
            downloadDBHelper = new DownloadDBHelper(context);
        }
        return downloadDBHelper;
    }

    private DownloadDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String download_sql = "create table thread_info (_id integer primary key autoincrement, " +
                "thread_id integer, url text, down_start integer, down_end integer, finished integer)";
        db.execSQL(download_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
