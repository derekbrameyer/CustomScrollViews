package com.doomonafireball.customscrollviews.widget;

import com.doomonafireball.customscrollviews.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class SyncedScrollView extends ScrollView
        implements SyncedScrollContainer.ScrollNotifier, SyncedScrollContainer.TouchNotifier {

    private SyncedScrollContainer.ScrollListener scrollListener = null;
    private SyncedScrollContainer.TouchListener touchListener = null;
    private boolean mDispatchScroll = true;
    private boolean mDispatchTouch = true;

    public SyncedScrollView(Context context) {
        super(context);
    }

    public SyncedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SyncedScrollView, 0, 0);
        mDispatchScroll = a.getBoolean(R.styleable.SyncedScrollView_dispatchScroll, true);
        mDispatchTouch = a.getBoolean(R.styleable.SyncedScrollView_dispatchTouch, true);
    }

    public SyncedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SyncedScrollView, defStyle, 0);
        mDispatchScroll = a.getBoolean(R.styleable.SyncedScrollView_dispatchScroll, true);
        mDispatchTouch = a.getBoolean(R.styleable.SyncedScrollView_dispatchTouch, true);
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

    public void setDispatchScroll(boolean b) {
        this.mDispatchScroll = b;
    }

    public boolean getDispatchScroll() {
        return this.mDispatchScroll;
    }

    public void setDispatchTouch(boolean b) {
        this.mDispatchTouch = b;
    }

    public boolean getDispatchTouch() {
        return this.mDispatchTouch;
    }

    @Override
    public void setScrollListener(SyncedScrollContainer.ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    public SyncedScrollContainer.ScrollListener getScrollListener() {
        return scrollListener;
    }

    @Override
    public void setTouchListener(SyncedScrollContainer.TouchListener touchListener) {
        this.touchListener = touchListener;
    }

    @Override
    public SyncedScrollContainer.TouchListener getTouchListener() {
        return touchListener;
    }
}