package com.hao.summaryproject.app;

import android.app.Application;

import com.hao.summaryproject.di.component.AppComponent;
import com.hao.summaryproject.di.component.DaggerAppComponent;
import com.hao.summaryproject.di.module.AppModule;
import com.hao.summaryproject.di.module.HttpModule;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public class SummaryApp extends Application {

    public static SummaryApp instance;

    public static AppComponent appComponent;

    public static synchronized SummaryApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent =  DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .httpModule(new HttpModule())
                    .build();
        }
        return appComponent;
    }
}
