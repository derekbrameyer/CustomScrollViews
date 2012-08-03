package com.doomonafireball.customscrollviews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * User: derek Date: 7/23/12 Time: 3:31 PM
 */
public class FrzMainListView extends ListView implements FrzSyncedScrollContainer.FrzTouchNotifier {

    private FrzSyncedScrollContainer.FrzTouchListener mTouchListener = null;
    private float xDistance, yDistance, lastX, lastY;

    public FrzMainListView(Context context) {
        super(context);
    }

    public FrzMainListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrzMainListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*@Override
    public void onScroll(AbsListView rag0, int arg1, int arg2, int arg3) {

    }

    /*@Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        if (mTouchListener != null) {
            mTouchListener.onTouchEvent(this, ev);
        }
        return true;
    }*/

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if(xDistance > yDistance)
                    return false;
        }

        return super.onInterceptTouchEvent(ev);
    }*/

    @Override
    public void setFrzTouchListener(FrzSyncedScrollContainer.FrzTouchListener touchListener) {
        this.mTouchListener = touchListener;
    }

    @Override
    public FrzSyncedScrollContainer.FrzTouchListener getFrzScrollListener() {
        return mTouchListener;
    }
}
