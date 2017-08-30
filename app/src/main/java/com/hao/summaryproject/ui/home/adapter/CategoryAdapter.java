package com.hao.summaryproject.ui.home.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hao.summaryproject.R;
import com.hao.summaryproject.app.Constants;
import com.hao.summaryproject.bean.HomeCategory;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by liuzhu
 * on 2017/8/23.
 */

public class CategoryAdapter extends RecyclerArrayAdapter<HomeCategory> {

    public CategoryAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(parent);
    }

    private class CategoryHolder extends BaseViewHolder<HomeCategory> {

        ImageView ivCategory;
        TextView tvTitle;
        public CategoryHolder(ViewGroup parent) {
            super(parent, R.layout.layout_home_category_item);
            ivCategory = $(R.id.category_icon);
            tvTitle = $(R.id.category_title);
        }

        @Override
        public void setData(HomeCategory homeCategory) {
            super.setData(homeCategory);

            tvTitle.setText(homeCategory.getName());
            String imgUrl = Constants.BASE_IMG_URL + homeCategory.getThumb();
            Glide.with(getContext()).load(imgUrl).into(ivCategory);
        }
    }
}
