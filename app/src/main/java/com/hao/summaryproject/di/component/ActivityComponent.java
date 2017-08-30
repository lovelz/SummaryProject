package com.hao.summaryproject.di.component;

import android.app.Activity;

import com.hao.summaryproject.MainActivity;
import com.hao.summaryproject.di.module.ActivityModule;
import com.hao.summaryproject.di.scope.ActivityScope;

import dagger.Component;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(MainActivity mainActivity);
}
