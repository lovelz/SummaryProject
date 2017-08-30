package com.hao.summaryproject.bean;

import java.io.Serializable;

/**
 * 首页Banner图
 * Created by liuzhu
 * on 2017/7/5.
 */

public class HomePagerData implements Serializable{

    private String goods;

    private String thumb;

    private String designtype;

    private String category;

    private String news;

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDesigntype() {
        return designtype;
    }

    public void setDesigntype(String designtype) {
        this.designtype = designtype;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }
}
