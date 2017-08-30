package com.hao.summaryproject.di.module;

import com.hao.summaryproject.app.SummaryApp;
import com.hao.summaryproject.http.DataManager;
import com.hao.summaryproject.http.HttpHelper;
import com.hao.summaryproject.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */
@Module
public class AppModule {

    private final SummaryApp summaryApp;

    public AppModule(SummaryApp summaryApp) {
        this.summaryApp = summaryApp;
    }

    @Provides
    @Singleton
    SummaryApp provideSummaryApp() {return summaryApp;}

    @Provides
    @Singleton
    HttpHelper provideHttpHelper(RetrofitHelper retrofitHelper) {
        return retrofitHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(HttpHelper httpHelper) {
        return new DataManager(httpHelper);
    }

}
