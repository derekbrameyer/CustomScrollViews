package com.doomonafireball.swankyscrollviews.listener;

/**
 * User: derek Date: 8/2/12 Time: 3:31 PM
 */
public interface SyncedTouchNotifier {

    public void setTouchListener(SyncedTouchListener touchListener);

    public SyncedTouchListener getTouchListener();

}
