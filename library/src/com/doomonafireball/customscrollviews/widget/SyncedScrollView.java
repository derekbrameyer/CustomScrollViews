package com.doomonafireball.customscrollviews.widget;

import com.doomonafireball.customscrollviews.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ScrollView;

import java.util.ArrayList;

public class SyncedScrollView extends ScrollView
        implements SyncedScrollNotifier, SyncedTouchNotifier {

    public static final String STICKY_TAG = "sticky";
    public static final String FLAG_NONCONSTANT = "-nonconstant";
    public static final String FLAG_HASTRANSPARENCY = "-hastransparency";

    private ArrayList<View> stickyViews;
    private View currentlyStickingView;
    private float stickyViewTopOffset;
    private boolean redirectTouchesToStickyView;
    private boolean clippingToPadding;
    private boolean clipToPaddingHasBeenSet;
    private boolean hasNotDoneActionDown = true;

    private final Runnable invalidateRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentlyStickingView != null) {
                int l = getLeftForViewRelativeOnlyChild(currentlyStickingView);
                int t = getBottomForViewRelativeOnlyChild(currentlyStickingView);
                int r = getRightForViewRelativeOnlyChild(currentlyStickingView);
                int b = (int) (getScrollY() + (currentlyStickingView.getHeight() + stickyViewTopOffset));
                invalidate(l, t, r, b);
            }
            postDelayed(this, 16);
        }
    };

    private ArrayList<SyncedScrollListener> scrollListeners = new ArrayList<SyncedScrollListener>();
    private SyncedTouchListener touchListener = null;
    private boolean mDispatchScroll = true;
    private boolean mDispatchTouch = true;
    private boolean mReceiveScroll = true;
    private boolean mReceiveTouch = true;
    private int mMaxScrollPosition = -1;

    public SyncedScrollView(Context context) {
        this(context, null);
    }

    public SyncedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.scrollViewStyle);
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

        // TODO I added this!
        setUp();
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
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
    public void addScrollListener(SyncedScrollListener scrollListener) {
        this.scrollListeners.add(scrollListener);
    }

    @Override
    public ArrayList<SyncedScrollListener> getScrollListeners() {
        return scrollListeners;
    }

    @Override
    public boolean isReceiver() {
        return this.mReceiveScroll;
    }

    @Override
    public void setTouchListener(SyncedTouchListener touchListener) {
        this.touchListener = touchListener;
    }

    @Override
    public SyncedTouchListener getTouchListener() {
        return touchListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (touchListener != null && mDispatchTouch) {
            touchListener.onTouchEvent(this, ev);
        }

        // TODO I added this!
        if (redirectTouchesToStickyView) {
            ev.offsetLocation(0,
                    ((getScrollY() + stickyViewTopOffset) - getTopForViewRelativeOnlyChild(currentlyStickingView)));
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            hasNotDoneActionDown = false;
        }

        if (hasNotDoneActionDown) {
            MotionEvent down = MotionEvent.obtain(ev);
            down.setAction(MotionEvent.ACTION_DOWN);
            super.onTouchEvent(down);
            hasNotDoneActionDown = false;
        }

        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            hasNotDoneActionDown = true;
        }
        // TODO End

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (this.mMaxScrollPosition > -1) {
            if (t <= this.mMaxScrollPosition) {
                super.onScrollChanged(l, t, oldl, oldt);
                if (mDispatchScroll) {
                    for (SyncedScrollListener listener : scrollListeners) {
                        listener.onScrollChanged(this, l, t, oldl, oldt);
                    }
                }
            } else {
                super.scrollTo(l, this.mMaxScrollPosition);
            }
        } else {
            super.onScrollChanged(l, t, oldl, oldt);
            /*if (scrollListener != null && mDispatchScroll) {
                scrollListener.onScrollChanged(this, l, t, oldl, oldt);
            }*/
            if (mDispatchScroll) {
                for (SyncedScrollListener listener : scrollListeners) {
                    listener.onScrollChanged(this, l, t, oldl, oldt);
                }
            }
        }

        // TODO I added this!
        doTheStickyThing();
    }

    private void setUp() {
        stickyViews = new ArrayList<View>();
    }

    private int getLeftForViewRelativeOnlyChild(View v) {
        int left = v.getLeft();
        while (v.getParent() != getChildAt(0)) {
            v = (View) v.getParent();
            left += v.getLeft();
        }
        return left;
    }

    private int getTopForViewRelativeOnlyChild(View v) {
        int top = v.getTop();
        while (v.getParent() != getChildAt(0)) {
            v = (View) v.getParent();
            top += v.getTop();
        }
        return top;
    }

    private int getRightForViewRelativeOnlyChild(View v) {
        int right = v.getRight();
        while (v.getParent() != getChildAt(0)) {
            v = (View) v.getParent();
            right += v.getRight();
        }
        return right;
    }

    private int getBottomForViewRelativeOnlyChild(View v) {
        int bottom = v.getBottom();
        while (v.getParent() != getChildAt(0)) {
            v = (View) v.getParent();
            bottom += v.getBottom();
        }
        return bottom;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            redirectTouchesToStickyView = true;
        }

        if (redirectTouchesToStickyView) {
            redirectTouchesToStickyView = currentlyStickingView != null;
            if (redirectTouchesToStickyView) {
                redirectTouchesToStickyView =
                        ev.getY() <= (currentlyStickingView.getHeight() + stickyViewTopOffset) &&
                                ev.getX() >= getLeftForViewRelativeOnlyChild(currentlyStickingView) &&
                                ev.getX() <= getRightForViewRelativeOnlyChild(currentlyStickingView);
            }
        } else if (currentlyStickingView == null) {
            redirectTouchesToStickyView = false;
        }
        if (redirectTouchesToStickyView) {
            ev.offsetLocation(0, -1 * ((getScrollY() + stickyViewTopOffset) - getTopForViewRelativeOnlyChild(
                    currentlyStickingView)));
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!clipToPaddingHasBeenSet) {
            clippingToPadding = true;
        }
        notifyHierarchyChanged();
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
        clippingToPadding = clipToPadding;
        clipToPaddingHasBeenSet = true;
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        findStickyViews(child);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (currentlyStickingView != null) {
            canvas.save();
            canvas.translate(getPaddingLeft(),
                    getScrollY() + stickyViewTopOffset + (clippingToPadding ? getPaddingTop() : 0));
            canvas.clipRect(0, (clippingToPadding ? -stickyViewTopOffset : 0), getWidth(),
                    currentlyStickingView.getHeight());
            if (getStringTagForView(currentlyStickingView).contains(FLAG_HASTRANSPARENCY)) {
                showView(currentlyStickingView);
                currentlyStickingView.draw(canvas);
                hideView(currentlyStickingView);
            } else {
                currentlyStickingView.draw(canvas);
            }
            canvas.restore();
        }
    }

    private void doTheStickyThing() {
        View viewThatShouldStick = null;
        View approachingView = null;
        for (View v : stickyViews) {
            int viewTop = getTopForViewRelativeOnlyChild(v) - getScrollY() + (clippingToPadding ? 0 : getPaddingTop());
            if (viewTop <= 0) {
                if (viewThatShouldStick == null || viewTop > (
                        getTopForViewRelativeOnlyChild(viewThatShouldStick) - getScrollY() + (clippingToPadding ? 0
                                : getPaddingTop()))) {
                    viewThatShouldStick = v;
                }
            } else {
                if (approachingView == null || viewTop < (getTopForViewRelativeOnlyChild(approachingView) - getScrollY()
                        + (clippingToPadding ? 0 : getPaddingTop()))) {
                    approachingView = v;
                }
            }
        }
        if (viewThatShouldStick != null) {
            stickyViewTopOffset = approachingView == null ? 0 : Math.min(0,
                    getTopForViewRelativeOnlyChild(approachingView) - getScrollY() + (clippingToPadding ? 0
                            : getPaddingTop()) - viewThatShouldStick.getHeight());
            if (viewThatShouldStick != currentlyStickingView) {
                if (currentlyStickingView != null) {
                    stopStickingCurrentlyStickingView();
                }
                startStickingView(viewThatShouldStick);
            }
        } else if (currentlyStickingView != null) {
            stopStickingCurrentlyStickingView();
        }
    }

    private void startStickingView(View viewThatShouldStick) {
        currentlyStickingView = viewThatShouldStick;
        if (getStringTagForView(currentlyStickingView).contains(FLAG_HASTRANSPARENCY)) {
            hideView(currentlyStickingView);
        }
        if (((String) currentlyStickingView.getTag()).contains(FLAG_NONCONSTANT)) {
            post(invalidateRunnable);
        }
    }

    private void stopStickingCurrentlyStickingView() {
        if (getStringTagForView(currentlyStickingView).contains(FLAG_HASTRANSPARENCY)) {
            showView(currentlyStickingView);
        }
        currentlyStickingView = null;
        removeCallbacks(invalidateRunnable);
    }

    /**
     * Notify that the sticky attribute has been added or removed from one or more views in the View hierarchy
     */
    public void notifyStickyAttributeChanged() {
        notifyHierarchyChanged();
    }

    private void notifyHierarchyChanged() {
        if (currentlyStickingView != null) {
            stopStickingCurrentlyStickingView();
        }
        stickyViews.clear();
        findStickyViews(getChildAt(0));
        doTheStickyThing();
        invalidate();
    }

    private void findStickyViews(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                String tag = getStringTagForView(vg.getChildAt(i));
                if (tag != null && tag.contains(STICKY_TAG)) {
                    stickyViews.add(vg.getChildAt(i));
                } else if (vg.getChildAt(i) instanceof ViewGroup) {
                    findStickyViews(vg.getChildAt(i));
                }
            }
        } else {
            String tag = (String) v.getTag();
            if (tag != null && tag.contains(STICKY_TAG)) {
                stickyViews.add(v);
            }
        }
    }

    private String getStringTagForView(View v) {
        Object tagObject = v.getTag();
        return String.valueOf(tagObject);
    }

    private void hideView(View v) {
        if (Build.VERSION.SDK_INT >= 11) {
            v.setAlpha(0);
        } else {
            AlphaAnimation anim = new AlphaAnimation(1, 0);
            anim.setDuration(0);
            anim.setFillAfter(true);
            v.startAnimation(anim);
        }
    }

    private void showView(View v) {
        if (Build.VERSION.SDK_INT >= 11) {
            v.setAlpha(1);
        } else {
            AlphaAnimation anim = new AlphaAnimation(0, 1);
            anim.setDuration(0);
            anim.setFillAfter(true);
            v.startAnimation(anim);
        }
    }
}