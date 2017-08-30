package com.hao.summaryproject.ui.home.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hao.summaryproject.R;
import com.hao.summaryproject.app.Constants;
import com.hao.summaryproject.bean.ThemeInfo;
import com.hao.summaryproject.util.StringUtils;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by liuzhu
 * on 2017/8/23.
 */

public class HomeAdapter extends RecyclerArrayAdapter<ThemeInfo> {

    public HomeAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeHolder(parent);
    }

    private class HomeHolder extends BaseViewHolder<ThemeInfo> {
        ImageView ivIcon;
        TextView tvInfo;
        TextView tvTime;

        private HomeHolder(ViewGroup parent) {
            super(parent, R.layout.layout_home_item);
            ivIcon = $(R.id.feature_icon);
            tvInfo = $(R.id.feature_info);
            tvTime = $(R.id.feature_time);
        }

        @Override
        public void setData(ThemeInfo themeInfo) {
            super.setData(themeInfo);

            String bannerUrl = Constants.BASE_IMG_URL + themeInfo.getThumb();
            tvInfo.setText(themeInfo.getName());
            tvTime.setText(StringUtils.formatTime(Long.parseLong(themeInfo.getUpdatetime())));
            Glide.with(getContext()).load(bannerUrl).into(ivIcon);
        }
    }
}
