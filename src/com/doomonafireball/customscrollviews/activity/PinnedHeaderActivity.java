package com.doomonafireball.customscrollviews.activity;

import com.doomonafireball.customscrollviews.R;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

import android.os.Bundle;

/**
 * User: derek Date: 7/16/12 Time: 3:51 PM
 */
public class PinnedHeaderActivity extends RoboSherlockFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinned_header);
    }
}
