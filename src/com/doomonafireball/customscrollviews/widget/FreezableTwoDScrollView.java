package com.doomonafireball.customscrollviews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * User: derek Date: 7/16/12 Time: 4:24 PM
 */
public class FreezableTwoDScrollView extends TwoDScrollView
        implements FreezableSyncedScrollContainer.ScrollNotifier, FreezableSyncedScrollContainer.TouchNotifier {

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
