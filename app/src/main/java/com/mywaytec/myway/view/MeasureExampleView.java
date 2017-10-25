package com.mywaytec.myway.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by shemh on 2017/5/2.
 */

public class MeasureExampleView extends View {

    public MeasureExampleView(Context context) {
        super(context);
    }

    public MeasureExampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int speSize = MeasureSpec.getSize(heightMeasureSpec);
        int speMode = MeasureSpec.getMode(heightMeasureSpec);
//        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), speSize);
//        setMeasuredDimension(100, 100);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        // 动态获取子View实例
//        for (int i = 0, size = getChildCount(); i < size; i++) {
//            View view = getChildAt(i);
//            // 放置子View，宽高都是100
//            view.layout(l, t, l + 100, t + 100);
//            l += 100 + padding;
//        }
    }
}
