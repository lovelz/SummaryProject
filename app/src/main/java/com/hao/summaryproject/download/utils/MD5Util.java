package com.hao.summaryproject.download.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.security.MessageDigest;

/**
 * @author lovelz
 * @date on 2018/4/8.
 */

public class MD5Util {

    public static String MD5(String pwd) {

        char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = pwd.getBytes();
            
            MessageDigest mdInput = MessageDigest.getInstance("MD5");
            mdInput.update(btInput);
            byte[] md = mdInput.digest();

            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 根据apk文件路径获取apk版本名称
     * @param context
     * @param archiveFilePath
     * @return
     */
    public static String getVersionNameFromApk(Context context, String archiveFilePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return null;
    }

}
