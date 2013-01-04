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
 * User: derek Date: 7/23/12 Time: 3:31 PM
 */
public class FrzSyncedScrollContainer extends RelativeLayout {

    private FrzColumnListView mColumnListView;
    private FrzMainListView mMainListView;
    private FrzHorizontalScrollView mHorizontalScrollView;

    private int mColumnListViewId;
    private int mMainListViewId;
    private int mHorizontalScrollViewId;

    FrzTouchManager mTouchManager;

    public FrzSyncedScrollContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrzSyncedScrollContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FrzSyncedScrollContainer, defStyle, 0);
        mColumnListViewId = a.getResourceId(R.styleable.FrzSyncedScrollContainer_frzColumnListViewId, 0);
        mMainListViewId = a.getResourceId(R.styleable.FrzSyncedScrollContainer_frzMainListViewId, 0);
        mHorizontalScrollViewId = a.getResourceId(R.styleable.FrzSyncedScrollContainer_frzHorizontalScrollViewId, 0);
        if (mColumnListViewId == 0) {
            throw new IllegalArgumentException("The frzColumnListViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        if (mMainListViewId == 0) {
            throw new IllegalArgumentException("The frzMainListViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        if (mHorizontalScrollViewId == 0) {
            throw new IllegalArgumentException("The frzHorizontalScrollViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        a.recycle();
        setAlwaysDrawnWithCacheEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        mTouchManager = new FrzTouchManager();

        mColumnListView = (FrzColumnListView) findViewById(mColumnListViewId);
        mMainListView = (FrzMainListView) findViewById(mMainListViewId);
        mHorizontalScrollView = (FrzHorizontalScrollView) findViewById(mHorizontalScrollViewId);
        if (mColumnListView == null) {
            throw new IllegalArgumentException("The frzColumnListViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        if (mMainListView == null) {
            throw new IllegalArgumentException("The frzMainListViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        if (mHorizontalScrollView == null) {
            throw new IllegalArgumentException("The frzHorizontalScrollViewId attribute is required and must refer "
                    + "to a valid child.");
        }

        // Always lock positions of all child views
        mTouchManager.addFrzTouchClient(mColumnListView);
        mTouchManager.addFrzTouchClient(mMainListView);
        mTouchManager.addFrzTouchClient(mHorizontalScrollView);
    }

    public interface FrzTouchNotifier {

        public void setFrzTouchListener(FrzTouchListener touchListener);

        public FrzTouchListener getFrzScrollListener();
    }

    public interface FrzTouchListener {

        boolean onTouchEvent(View syncedScrollView, MotionEvent ev);
    }

    public class FrzTouchManager implements FrzTouchListener {

        private ArrayList<FrzTouchNotifier> clients = new ArrayList<FrzTouchNotifier>(3);
        private volatile boolean isSyncing = false;

        public void addFrzTouchClient(FrzTouchNotifier client) {
            clients.add(client);
            client.setFrzTouchListener(this);
        }

        @Override
        public boolean onTouchEvent(View sender, MotionEvent ev) {
            // avoid notifications while scroll bars are being synchronized
            if (isSyncing) {
                return false;
            }
            isSyncing = true;
            if (sender instanceof FrzHorizontalScrollView) {
                ((FrzColumnListView) clients.get(0)).onTouchEvent(ev);
                ((FrzMainListView) clients.get(1)).onTouchEvent(ev);
            } else if (sender instanceof FrzMainListView) {
                // update column and horiz if necessary
                ((FrzColumnListView) clients.get(0)).onTouchEvent(ev);
                //((FrzHorizontalScrollView) clients.get(2)).onTouchEvent(ev);
            } else if (sender instanceof FrzColumnListView) {
                // update main
                //((FrzMainListView) clients.get(1)).onTouchEvent(ev);
                ((FrzHorizontalScrollView) clients.get(2)).onTouchEvent(ev);
            }

            /*for (FrzTouchNotifier client : clients) {
                View view = (View) client;
                if (view == sender) {
                    continue;
                }
                view.onTouchEvent(ev);
            }*/
            isSyncing = false;
            return false;
        }
    }
}
