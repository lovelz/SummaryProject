package com.hao.summaryproject.download.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hao.summaryproject.download.bean.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lovelz
 * @date on 2018/4/7.
 */

public class ThreadDaoImpl implements ThreadDao {

    private DownloadDBHelper dbHelper;

    public ThreadDaoImpl(Context context) {
        dbHelper = DownloadDBHelper.getInstance(context);
    }

    @Override
    public synchronized void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into thread_info ( thread_id, url, down_start, down_end, finished) values (?, ?, ?, ?, ?)",
                new Object[]{threadInfo.getId(), threadInfo.getUrl(), threadInfo.getStart(), threadInfo.getEnd(), threadInfo.getFinished()});
        db.close();
    }

    @Override
    public synchronized void updateThread(int threadId, String url, int finished) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update thread_info set finished = ? where thread_id = ? and url = ?", new Object[]{finished, threadId, url});
        db.close();
    }

    @Override
    public synchronized void deleteThread(String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from thread_info where url = ?", new Object[]{url});
        db.close();
    }

    @Override
    public synchronized List<ThreadInfo> getAllThread(String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url = ?", new String[]{url});
        List<ThreadInfo> threadInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("down_start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("down_end")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            threadInfoList.add(threadInfo);
        }
        cursor.close();
        db.close();
        return threadInfoList;
    }
}
