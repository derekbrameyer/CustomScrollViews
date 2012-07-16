package com.doomonafireball.customscrollviews.widget;

import com.doomonafireball.customscrollviews.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * User: derek Date: 6/29/12 Time: 11:47 AM
 */
public class SyncedScrollContainer extends RelativeLayout {

    private SyncedScrollView mOverScrollView;
    private SyncedScrollView mUnderScrollView;
    private SyncedScrollView mThirdScrollView;
    private SyncedScrollView mFourthScrollView;

    private int mOverScrollViewId;
    private int mUnderScrollViewId;
    private int mThirdScrollViewId;
    private int mFourthScrollViewId;

    private boolean mLockScrolling;
    private boolean mSyncScroll;
    private boolean mSyncTouch;
    private float mScrollFactor;
    private float mSecondScrollFactor;
    private float mThirdScrollFactor;

    ScrollManager mScrollManager;
    TouchManager mTouchManager;

    public SyncedScrollContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SyncedScrollContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SyncedScrollContainer, defStyle, 0);
        int overScrollViewId = a.getResourceId(R.styleable.SyncedScrollContainer_overScrollViewId, 0);
        if (overScrollViewId == 0) {
            throw new IllegalArgumentException("The overScrollViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        int underScrollViewId = a.getResourceId(R.styleable.SyncedScrollContainer_underScrollViewId, 0);
        if (underScrollViewId == 0) {
            throw new IllegalArgumentException("The underScrollViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        int thirdScrollViewId = a.getResourceId(R.styleable.SyncedScrollContainer_thirdScrollViewId, 0);
        int fourthScrollViewId = a.getResourceId(R.styleable.SyncedScrollContainer_fourthScrollViewId, 0);
        mScrollFactor = a.getFloat(R.styleable.SyncedScrollContainer_scrollFactor, 1.0f);
        mSecondScrollFactor = a.getFloat(R.styleable.SyncedScrollContainer_secondScrollFactor, 1.0f);
        mThirdScrollFactor = a.getFloat(R.styleable.SyncedScrollContainer_thirdScrollFactor, 1.0f);
        mLockScrolling = a.getBoolean(R.styleable.SyncedScrollContainer_lockScrolling, true);
        mSyncScroll = a.getBoolean(R.styleable.SyncedScrollContainer_syncScroll, true);
        mSyncTouch = a.getBoolean(R.styleable.SyncedScrollContainer_syncTouch, false);

        mOverScrollViewId = overScrollViewId;
        mUnderScrollViewId = underScrollViewId;
        mThirdScrollViewId = thirdScrollViewId;
        mFourthScrollViewId = fourthScrollViewId;

        a.recycle();

        setAlwaysDrawnWithCacheEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        mScrollManager = new ScrollManager();
        mTouchManager = new TouchManager();

        mOverScrollView = (SyncedScrollView) findViewById(mOverScrollViewId);
        if (mOverScrollView == null) {
            throw new IllegalArgumentException("The overScrollViewId attribute is must refer to an"
                    + " existing child.");
        }
        mUnderScrollView = (SyncedScrollView) findViewById(mUnderScrollViewId);
        if (mUnderScrollView == null) {
            throw new IllegalArgumentException("The underScrollViewId attribute is must refer to an"
                    + " existing child.");
        }
        if (mThirdScrollViewId != 0) {
            mThirdScrollView = (SyncedScrollView) findViewById(mThirdScrollViewId);
        }
        if (mFourthScrollViewId != 0) {
            mFourthScrollView = (SyncedScrollView) findViewById(mFourthScrollViewId);
        }

        if (mSyncScroll) {
            mScrollManager.addScrollClient(mOverScrollView);
            mScrollManager.addScrollClient(mUnderScrollView);
            if (mThirdScrollView != null) {
                mScrollManager.addScrollClient(mThirdScrollView);
            }
            if (mFourthScrollView != null) {
                mScrollManager.addScrollClient(mFourthScrollView);
            }
        }
        if (mSyncTouch) {
            mTouchManager.addTouchClient(mOverScrollView);
            mTouchManager.addTouchClient(mUnderScrollView);
            if (mThirdScrollView != null) {
                mTouchManager.addTouchClient(mThirdScrollView);
            }
            if (mFourthScrollView != null) {
                mTouchManager.addTouchClient(mFourthScrollView);
            }
        }
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

        private ArrayList<TouchNotifier> clients = new ArrayList(4);

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

        private ArrayList<ScrollNotifier> clients = new ArrayList(4);

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

            // update clients
            for (ScrollNotifier client : clients) {
                View view = (View) client;
                // don't update sender
                if (view == sender) {
                    continue;
                }

                // scroll relevant views only
                // TODO Add support for horizontal ListViews - currently weird things happen when ListView is being scrolled horizontally
                if ((scrollType == SCROLL_HORIZONTAL && view instanceof HorizontalScrollView)
                        || (scrollType == SCROLL_VERTICAL && view instanceof ScrollView)
                        || (scrollType == SCROLL_VERTICAL && view instanceof ListView)) {
                    if (mLockScrolling) {
                        view.scrollTo((int) (mScrollFactor * l), (int) (mScrollFactor * t));
                    } else {
                        // TODO Figure out why scrollBy doesn't quite work when not flung
                        view.scrollBy((int) (mScrollFactor * (l - oldl)), (int) (mScrollFactor * (t - oldt)));
                    }
                }
            }
            /*for (int i = 0; i < clients.size(); i++) {
                View view = clients.get(i);
            }*/
            isSyncing = false;
        }
    }
}
