package com.hao.summaryproject.di.component;

import android.app.Activity;

import com.hao.summaryproject.di.module.FragmentModule;
import com.hao.summaryproject.di.scope.FragmentScope;
import com.hao.summaryproject.ui.home.HomeFragment;

import dagger.Component;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(HomeFragment homeFragment);

}
