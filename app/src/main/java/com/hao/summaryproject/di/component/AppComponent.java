package com.hao.summaryproject.di.component;

import com.hao.summaryproject.app.SummaryApp;
import com.hao.summaryproject.di.module.AppModule;
import com.hao.summaryproject.di.module.HttpModule;
import com.hao.summaryproject.http.DataManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */
@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {

    SummaryApp getSummaryApp();

    DataManager getDataManager();

}
