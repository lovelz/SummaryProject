package com.hao.summaryproject.bean.base;

/**
 * 获取json数据的基类
 * Created by liuzhu
 * on 2017/5/19.
 */

public class BaseBean<T> {

    private String status;

    private String message;

    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
