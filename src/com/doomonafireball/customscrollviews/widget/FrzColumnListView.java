package com.doomonafireball.customscrollviews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * User: derek Date: 7/23/12 Time: 3:31 PM
 */
public class FrzColumnListView extends ListView implements FrzSyncedScrollContainer.FrzTouchNotifier {

    private FrzSyncedScrollContainer.FrzTouchListener mTouchListener = null;

    public FrzColumnListView(Context context) {
        super(context);
    }

    public FrzColumnListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrzColumnListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        if (mTouchListener != null) {
            mTouchListener.onTouchEvent(this, ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public void setFrzTouchListener(FrzSyncedScrollContainer.FrzTouchListener touchListener) {
        this.mTouchListener = touchListener;
    }

    @Override
    public FrzSyncedScrollContainer.FrzTouchListener getFrzScrollListener() {
        return mTouchListener;
    }
}