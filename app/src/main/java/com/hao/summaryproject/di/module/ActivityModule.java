package com.hao.summaryproject.di.module;

import android.app.Activity;

import com.hao.summaryproject.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */
@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }
}
