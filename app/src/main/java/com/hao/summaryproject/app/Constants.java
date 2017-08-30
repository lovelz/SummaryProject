package com.hao.summaryproject.app;

import java.io.File;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public class Constants {

    public static final String APP_LOG = "SummaryProject";

    //------------------------ url -------------------------
    public static final String BASE_URL = "http://106.14.139.193/getdata/";
    public static final String BASE_IMG_URL = "http://maoyiquan.oss-cn-shanghai.aliyuncs.com/";

    //------------------------ path ------------------------
    public static final String BASE_PATH = SummaryApp.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String CACHE_PATH = BASE_PATH + "/NetCache";
}
