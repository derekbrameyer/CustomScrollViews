package com.doomonafireball.customscrollviews.widget;

import com.doomonafireball.customscrollviews.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * User: derek Date: 6/29/12 Time: 11:47 AM
 */
public class SyncedListContainer extends RelativeLayout {

    private SyncedListView mOverListView;
    private SyncedListView mUnderListView;

    private int mOverListViewId;
    private int mUnderListViewId;

    private boolean mLockScrolling;
    private float mScrollFactor;

    TouchManager mTouchManager;
    SelectionManager mSelectionManager;

    public SyncedListContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SyncedListContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SyncedScrollContainer, defStyle, 0);
        int overListViewId = a.getResourceId(R.styleable.SyncedScrollContainer_overListViewId, 0);
        if (overListViewId == 0) {
            throw new IllegalArgumentException("The overListViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        int underListViewId = a.getResourceId(R.styleable.SyncedScrollContainer_underListViewId, 0);
        if (underListViewId == 0) {
            throw new IllegalArgumentException("The underListViewId attribute is required and must refer "
                    + "to a valid child.");
        }
        mScrollFactor = a.getFloat(R.styleable.SyncedScrollContainer_scrollFactor, 1.0f);
        mLockScrolling = a.getBoolean(R.styleable.SyncedScrollContainer_lockScrolling, true);

        mOverListViewId = overListViewId;
        mUnderListViewId = underListViewId;

        a.recycle();

        setAlwaysDrawnWithCacheEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        mTouchManager = new TouchManager();
        mSelectionManager = new SelectionManager();

        mOverListView = (SyncedListView) findViewById(mOverListViewId);
        if (mOverListView == null) {
            throw new IllegalArgumentException("The overListViewId attribute is must refer to an"
                    + " existing child.");
        }
        mTouchManager.addTouchClient(mOverListView);
        mSelectionManager.addSelectionClient(mOverListView);

        mUnderListView = (SyncedListView) findViewById(mUnderListViewId);
        if (mUnderListView == null) {
            throw new IllegalArgumentException("The underListViewId attribute is must refer to an"
                    + " existing child.");
        }
        mTouchManager.addTouchClient(mUnderListView);
        mSelectionManager.addSelectionClient(mUnderListView);
    }

    public interface SelectionNotifier {

        public void setSelectionListener(SelectionListener selectionListener);

        public SelectionListener getSelectionListener();
    }

    public interface SelectionListener {

        void smoothScrollToPosition(View syncedListView, int position);

        void smoothScrollByOffset(View syncedListView, int offset);

        void setSelection(View syncedListView, int position);

        void setSelectionFromTop(View syncedListView, int position, int y);

        void setSelectionAfterHeaderView(View syncedListView);
    }

    public class SelectionManager implements SelectionListener {

        private ArrayList<SelectionNotifier> clients = new ArrayList<SelectionNotifier>(4);

        private volatile boolean isSyncing = false;

        public void addSelectionClient(SelectionNotifier client) {
            clients.add(client);
            client.setSelectionListener(this);
        }

        @Override
        public void smoothScrollToPosition(View sender, int position) {
            if (isSyncing) {
                return;
            }

            isSyncing = true;

            // update clients
            for (SelectionNotifier client : clients) {
                View view = (View) client;
                // don't update sender
                if (view == sender) {
                    continue;
                }

                ((SyncedListView) view).smoothScrollToPosition(position);
            }
            isSyncing = false;
        }

        @Override
        public void smoothScrollByOffset(View sender, int offset) {
            if (isSyncing) {
                return;
            }

            isSyncing = true;

            // update clients
            for (SelectionNotifier client : clients) {
                View view = (View) client;
                // don't update sender
                if (view == sender) {
                    continue;
                }

                ((SyncedListView) view).smoothScrollByOffset(offset);
            }
            isSyncing = false;
        }

        @Override
        public void setSelection(View sender, int position) {
            if (isSyncing) {
                return;
            }

            isSyncing = true;

            // update clients
            for (SelectionNotifier client : clients) {
                View view = (View) client;
                // don't update sender
                if (view == sender) {
                    continue;
                }

                ((SyncedListView) view).setSelection(position);
            }
            isSyncing = false;
        }

        @Override
        public void setSelectionFromTop(View sender, int position, int y) {
            if (isSyncing) {
                return;
            }

            isSyncing = true;

            // update clients
            for (SelectionNotifier client : clients) {
                View view = (View) client;
                // don't update sender
                if (view == sender) {
                    continue;
                }

                ((SyncedListView) view).setSelectionFromTop(position, y);
            }
            isSyncing = false;
        }

        @Override
        public void setSelectionAfterHeaderView(View sender) {
            if (isSyncing) {
                return;
            }

            isSyncing = true;

            // update clients
            for (SelectionNotifier client : clients) {
                View view = (View) client;
                // don't update sender
                if (view == sender) {
                    continue;
                }

                ((SyncedListView) view).setSelectionAfterHeaderView();
            }
            isSyncing = false;
        }
    }

    public interface TouchNotifier {

        public void setTouchListener(TouchListener touchListener);

        public TouchListener getTouchListener();
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
}
