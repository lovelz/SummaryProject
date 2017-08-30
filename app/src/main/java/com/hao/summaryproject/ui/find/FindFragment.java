package com.hao.summaryproject.ui.find;

import android.view.View;

import com.hao.summaryproject.R;
import com.hao.summaryproject.base.BaseFragment;
import com.hao.summaryproject.view.AnimationButton;

import butterknife.BindView;

/**
 * 找款
 * Created by liuzhu
 * on 2017/8/22.
 */

public class FindFragment extends BaseFragment {
//    @BindView(R.id.view_animation)
//    AnimationButton viewAnimation;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void initEvent() {
        super.initEvent();

//        viewAnimation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewAnimation.start();
//            }
//        });
    }
}
