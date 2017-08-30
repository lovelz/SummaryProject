package com.hao.summaryproject.di.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.hao.summaryproject.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */
@Module
public class FragmentModule {

    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return mFragment.getActivity();
    }
}
