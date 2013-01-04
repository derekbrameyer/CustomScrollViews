package com.doomonafireball.swankyscrollviews.widget;

import com.doomonafireball.customscrollviews.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * User: derek Date: 7/16/12 Time: 4:25 PM
 */
public class FreezableSyncedScrollContainer extends RelativeLayout {

    private FreezableHorizontalScrollView mHorizontalScrollView;
    private PinnedHeaderScrollView mVerticalScrollView;
    private FreezableTwoDScrollView mTwoDScrollView;

    private int mHorizontalScrollViewId;
    private int mVerticalScrollViewId;
    private int mTwoDScrollViewId;

    ScrollManager mScrollManager;
    TouchManager mTouchManager;

    public FreezableSyncedScrollContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreezableSyncedScrollContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FreezableSyncedScrollContainer, defStyle, 0);
        mHorizontalScrollViewId = a.getResourceId(R.styleable.FreezableSyncedScrollContainer_horizontalScrollViewId, 0);
        if (mHorizontalScrollViewId == 0) {
            throw new IllegalArgumentException("The horizontalScrollViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        mVerticalScrollViewId = a.getResourceId(R.styleable.FreezableSyncedScrollContainer_verticalScrollViewId, 0);
        if (mVerticalScrollViewId == 0) {
            throw new IllegalArgumentException("The verticalScrollViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        mTwoDScrollViewId = a.getResourceId(R.styleable.FreezableSyncedScrollContainer_twoDScrollViewId, 0);
        if (mTwoDScrollViewId == 0) {
            throw new IllegalArgumentException("The twoDScrollViewId attribute is required and must refer "
                    + "to a valid child.");
        }

        a.recycle();

        setAlwaysDrawnWithCacheEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        mScrollManager = new ScrollManager();
        mTouchManager = new TouchManager();

        mHorizontalScrollView = (FreezableHorizontalScrollView) findViewById(mHorizontalScrollViewId);
        if (mHorizontalScrollView == null) {
            throw new IllegalArgumentException("The mHorizontalScrollViewId attribute is required and must refer to an"
                    + " existing child.");
        }
        mVerticalScrollView = (PinnedHeaderScrollView) findViewById(mVerticalScrollViewId);
        if (mVerticalScrollView == null) {
            throw new IllegalArgumentException("The mVerticalScrollViewId attribute is required and must refer to an"
                    + " existing child.");
        }
        mTwoDScrollView = (FreezableTwoDScrollView) findViewById(mTwoDScrollViewId);
        if (mVerticalScrollView == null) {
            throw new IllegalArgumentException("The mTwoDScrollViewId attribute is required and must refer to an"
                    + " existing child.");
        }

        // Always lock positions of all child views
        mScrollManager.addScrollClient(mHorizontalScrollView);
        mScrollManager.addScrollClient(mVerticalScrollView);
        mScrollManager.addScrollClient(mTwoDScrollView);

        // Always lock touches of all child views (?)
        //mTouchManager.addTouchClient(mHorizontalScrollView);
        //mTouchManager.addTouchClient(mVerticalScrollView);
        //mTouchManager.addTouchClient(mTwoDScrollView);
    }

    public interface ScrollNotifier {

        public void setScrollListener(ScrollListener scrollListener);

        public ScrollListener getScrollListener();
    }

    public interface TouchNotifier {

        public void setTouchListener(TouchListener touchListener);

        public TouchListener getTouchListener();
    }

    public interface ScrollListener {

        void onScrollChanged(View syncedScrollView, int l, int t, int oldl, int oldt);
    }

    public interface TouchListener {

        boolean onTouchEvent(View syncedScrollView, MotionEvent ev);
    }

    public class TouchManager implements TouchListener {

        private ArrayList<TouchNotifier> clients = new ArrayList<TouchNotifier>();

        private volatile boolean isSyncing = false;

        public void addTouchClient(TouchNotifier client) {
            clients.add(client);
            client.setTouchListener(this);
        }

        @Override
        public boolean onTouchEvent(View sender, MotionEvent ev) {
            // avoid notifications while scroll bars are being synchronized
            if (isSyncing) {
                return false;
            }

            isSyncing = true;

            // update clients
            for (TouchNotifier client : clients) {
                View view = (View) client;
                // don't update sender
                if (view == sender) {
                    continue;
                }

                view.onTouchEvent(ev);
            }
            isSyncing = false;
            return false;
        }
    }

    public class ScrollManager implements ScrollListener {

        private static final int SCROLL_HORIZONTAL = 1;
        private static final int SCROLL_VERTICAL = 2;

        private ArrayList<ScrollNotifier> clients = new ArrayList<ScrollNotifier>();

        private volatile boolean isSyncing = false;
        private int scrollType = SCROLL_HORIZONTAL;

        public void addScrollClient(ScrollNotifier client) {
            clients.add(client);
            client.setScrollListener(this);
        }

        // TODO fix dependency on all views being of equal horizontal/ vertical dimensions
        @Override
        public void onScrollChanged(View sender, int l, int t, int oldl, int oldt) {
            // avoid notifications while scroll bars are being synchronized
            if (isSyncing) {
                return;
            }

            isSyncing = true;

            // remember scroll type
            if (l != oldl) {
                scrollType = SCROLL_HORIZONTAL;
            } else if (t != oldt) {
                scrollType = SCROLL_VERTICAL;
            } else {
                // not sure why this should happen
                isSyncing = false;
                return;
            }

            if (sender instanceof FreezableHorizontalScrollView) {
                // update the vertical and twoD
                //((FreezableVerticalScrollView) clients.get(1)).scrollTo(l, t);
                ((FreezableTwoDScrollView) clients.get(2)).setScrollX(l);
                //((FreezableTwoDScrollView) clients.get(2)).scrollTo(l, t);
            } else if (sender instanceof PinnedHeaderScrollView) {
                // update the horizontal and twoD
                //((FreezableHorizontalScrollView) clients.get(0)).scrollTo(l, t);
                ((FreezableTwoDScrollView) clients.get(2)).setScrollY(t);
            } else {
                // update the horizontal and vertical
                //((FreezableHorizontalScrollView) clients.get(0)).scrollTo(l, t);
                //((FreezableVerticalScrollView) clients.get(1)).scrollTo(l, t);
                ((FreezableHorizontalScrollView) clients.get(0)).setScrollX(l);
                ((PinnedHeaderScrollView) clients.get(1)).setScrollY(t);
            }

            // update clients
            /*for (ScrollNotifier client : clients) {
                View view = (View) client;
                // don't update sender
                if (view == sender) {
                    continue;
                }

                if (view instanceof FreezableHorizontalScrollView) {
                    view.scrollTo(l, ((TwoDScrollView) clients.get(2)).getScrollY());
                } else if (view instanceof FreezableVerticalScrollView) {
                    view.scrollTo(((TwoDScrollView) clients.get(2)).getScrollX(), t);
                } else if (view instanceof FreezableTwoDScrollView) {
                    view.scrollTo(l, t);
                }
            }*/
            isSyncing = false;
        }
    }
}
