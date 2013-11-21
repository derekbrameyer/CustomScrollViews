package com.doomonafireball.customscrollviews.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * User: derek Date: 8/2/12 Time: 3:30 PM
 */
public class SyncedScrollManager implements SyncedScrollListener, SyncedTouchListener {

    private static final int SCROLL_HORIZONTAL = 1;
    private static final int SCROLL_VERTICAL = 2;

    private boolean mLockScrolling;
    private float mScrollFactor;

    private ArrayList<SyncedScrollNotifier> scrollClients = new ArrayList<SyncedScrollNotifier>();
    private ArrayList<SyncedTouchNotifier> touchClients = new ArrayList<SyncedTouchNotifier>();

    private volatile boolean isSyncing = false;
    private int currentScrollY = 0;

    public SyncedScrollManager(boolean mLockScrolling, float mScrollFactor) {
        this.mLockScrolling = mLockScrolling;
        this.mScrollFactor = mScrollFactor;
    }

    public void addScrollClient(SyncedScrollNotifier client) {
        scrollClients.add(client);
        client.addScrollListener(this);
    }

    public void addTouchClient(SyncedTouchNotifier client) {
        touchClients.add(client);
        client.setTouchListener(this);
    }

    public int getCurrentScrollY() {
        return currentScrollY;
    }

    @Override
    public void onScrollChanged(View sender, int l, int t, int oldl, int oldt) {
        // TODO fix dependency on all views being of equal horizontal/ vertical dimensions

        // avoid notifications while scroll bars are being synchronized
        if (isSyncing) {
            return;
        }

        isSyncing = true;

        // remember scroll type
        int scrollType = SCROLL_HORIZONTAL;
        if (l != oldl) {
            scrollType = SCROLL_HORIZONTAL;
        } else if (t != oldt) {
            scrollType = SCROLL_VERTICAL;
        } else {
            // not sure why this should happen
            isSyncing = false;
            return;
        }

        // update scrollClients
        for (SyncedScrollNotifier client : scrollClients) {
            View view = (View) client;
            // don't update sender
            if (view == sender) {
                currentScrollY = l;
                continue;
            }

            if (client.isReceiver()) {
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
        }
        isSyncing = false;
    }

    @Override
    public void onTouchEvent(View sender, MotionEvent ev) {
        // avoid notifications while scroll bars are being synchronized
        if (isSyncing) {
            return;
        }

        isSyncing = true;
        for (SyncedTouchNotifier client : touchClients) {
            View view = (View) client;
            if (view == sender) {
                break;
            }

            view.onTouchEvent(ev);
        }
        isSyncing = false;
    }
}
