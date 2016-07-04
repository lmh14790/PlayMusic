package com.yztc.call_back;

import android.view.View;

/**
 * Created by Administrator on 2016/7/1.
 */
public interface OnClickCallBack<T> {
    public void click(T view,int position);
}
