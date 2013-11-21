package com.doomonafireball.customscrollviews.listener;

import java.util.ArrayList;

/**
 * User: derek Date: 8/2/12 Time: 3:31 PM
 */
public interface SyncedScrollNotifier {

    public void addScrollListener(SyncedScrollListener scrollListener);

    public ArrayList<SyncedScrollListener> getScrollListeners();

    public boolean isReceiver();

}
