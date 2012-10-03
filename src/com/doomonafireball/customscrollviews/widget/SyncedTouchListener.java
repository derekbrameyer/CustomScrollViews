package com.doomonafireball.customscrollviews.widget;

import android.view.MotionEvent;
import android.view.View;

/**
 * User: derek Date: 8/2/12 Time: 3:31 PM
 */
public interface SyncedTouchListener {

    void onTouchEvent(View syncedScrollView, MotionEvent ev);

}
