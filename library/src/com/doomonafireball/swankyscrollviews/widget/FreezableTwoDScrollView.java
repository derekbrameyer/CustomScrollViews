package com.doomonafireball.swankyscrollviews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * User: derek Date: 7/16/12 Time: 4:24 PM
 */
public class FreezableTwoDScrollView extends TwoDScrollView
        implements FreezableSyncedScrollContainer.ScrollNotifier, FreezableSyncedScrollContainer.TouchNotifier {

    private View mCurrentPinView;
    private View mCurrentHeaderView;

    private FreezableSyncedScrollContainer.ScrollListener scrollListener = null;
    private FreezableSyncedScrollContainer.TouchListener touchListener = null;
    private boolean mDispatchScroll = true;
    private boolean mDispatchTouch = true;

    public FreezableTwoDScrollView(Context context) {
        super(context);
    }

    public FreezableTwoDScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FreezableTwoDScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addPinView(View v) {
        mCurrentPinView = v;
        syncViews();
    }

    public void addHeaderView(View v) {
        mCurrentHeaderView = v;
        syncViews();
    }

    private void syncViews() {
        if (mCurrentPinView == null || mCurrentHeaderView == null) {
            return;
        }

        // Distance between pin view and header view
        int distance = mCurrentPinView.getTop() - mCurrentHeaderView.getTop();
        mCurrentHeaderView.offsetTopAndBottom(distance);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        syncViews();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        if (touchListener != null && mDispatchTouch) {
            touchListener.onTouchEvent(this, ev);
        }
        return true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener != null && mDispatchScroll) {
            scrollListener.onScrollChanged(this, l, t, oldl, oldt);
        }

        if (mCurrentPinView == null || mCurrentHeaderView == null) {
            return;
        }

        // Distance between the pin view and the scroll position
        int matchDistance = mCurrentPinView.getTop() - getScrollY();
        // Distance between scroll position and header view
        int offset = getScrollY() - mCurrentHeaderView.getTop();
        // Check if pin is scrolled off screen
        if (matchDistance < 0) {
            mCurrentHeaderView.offsetTopAndBottom(offset);
        } else {
            syncViews();
        }
    }

    @Override
    public void setScrollListener(FreezableSyncedScrollContainer.ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    public FreezableSyncedScrollContainer.ScrollListener getScrollListener() {
        return scrollListener;
    }

    @Override
    public void setTouchListener(FreezableSyncedScrollContainer.TouchListener touchListener) {
        this.touchListener = touchListener;
    }

    @Override
    public FreezableSyncedScrollContainer.TouchListener getTouchListener() {
        return touchListener;
    }
}
