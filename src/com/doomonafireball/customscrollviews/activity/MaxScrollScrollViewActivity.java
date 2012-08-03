package com.doomonafireball.customscrollviews.activity;

import com.doomonafireball.customscrollviews.MainApp;
import com.doomonafireball.customscrollviews.R;
import com.doomonafireball.customscrollviews.widget.SyncedScrollView;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import roboguice.inject.InjectView;

/**
 * User: derek Date: 6/29/12 Time: 4:01 PM
 */
public class MaxScrollScrollViewActivity extends RoboSherlockFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MainApp.TAG, "onCreate");
        setContentView(R.layout.max_scroll_sv);
    }
}
