package com.doomonafireball.customscrollviews.widget;

import com.doomonafireball.customscrollviews.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class SyncedScrollView extends ScrollView
        implements SyncedScrollContainer.ScrollNotifier, SyncedScrollContainer.TouchNotifier {

    private SyncedScrollContainer.ScrollListener scrollListener = null;
    private SyncedScrollContainer.TouchListener touchListener = null;
    private boolean mDispatchScroll = true;
    private boolean mDispatchTouch = true;
    private boolean mReceiveScroll = true;
    private boolean mReceiveTouch = true;
    private int mMaxScrollPosition = -1;

    public SyncedScrollView(Context context) {
        super(context);
    }

    public SyncedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SyncedScrollView, 0, 0);
        mDispatchScroll = a.getBoolean(R.styleable.SyncedScrollView_dispatchScroll, true);
        mDispatchTouch = a.getBoolean(R.styleable.SyncedScrollView_dispatchTouch, true);
        mReceiveScroll = a.getBoolean(R.styleable.SyncedScrollView_receiveScroll, true);
        mReceiveTouch = a.getBoolean(R.styleable.SyncedScrollView_receiveTouch, true);
        float maxScrollPosition = a.getDimension(R.styleable.SyncedScrollView_maxScrollPosition, -1.0f);
        mMaxScrollPosition = convertDpToPixel(maxScrollPosition, context);
    }

    public SyncedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SyncedScrollView, defStyle, 0);
        mDispatchScroll = a.getBoolean(R.styleable.SyncedScrollView_dispatchScroll, true);
        mDispatchTouch = a.getBoolean(R.styleable.SyncedScrollView_dispatchTouch, true);
        mReceiveScroll = a.getBoolean(R.styleable.SyncedScrollView_receiveScroll, true);
        mReceiveTouch = a.getBoolean(R.styleable.SyncedScrollView_receiveTouch, true);
        float maxScrollPosition = a.getDimension(R.styleable.SyncedScrollView_maxScrollPosition, -1.0f);
        mMaxScrollPosition = convertDpToPixel(maxScrollPosition, context);
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
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
        if (this.mMaxScrollPosition > -1) {
            if (t <= this.mMaxScrollPosition) {
                super.onScrollChanged(l, t, oldl, oldt);
                if (scrollListener != null && mDispatchScroll) {
                    scrollListener.onScrollChanged(this, l, t, oldl, oldt);
                }
            } else {
                super.scrollTo(l, this.mMaxScrollPosition);
            }
        } else {
            super.onScrollChanged(l, t, oldl, oldt);
            if (scrollListener != null && mDispatchScroll) {
                scrollListener.onScrollChanged(this, l, t, oldl, oldt);
            }
        }
    }

    public void setMaxScrollPosition(int i) {
        this.mMaxScrollPosition = i;
    }

    public int getMaxScrollPosition() {
        return this.mMaxScrollPosition;
    }

    public void setReceiveScroll(boolean b) {
        this.mReceiveScroll = b;
    }

    public boolean getReceiveScroll() {
        return this.mReceiveScroll;
    }

    public void setReceiveTouch(boolean b) {
        this.mReceiveTouch = b;
    }

    public boolean getReceiveTouch() {
        return this.mReceiveTouch;
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