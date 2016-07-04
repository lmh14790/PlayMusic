package com.yztc.customView;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/7/1.
 */
public class RecycleViewDivider extends RecyclerView.ItemDecoration{
    private int space;

    public RecycleViewDivider(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildLayoutPosition(view)!=0){
            outRect.top = space;
        }
        outRect.left=space;
        outRect.right=space;
    }
}
