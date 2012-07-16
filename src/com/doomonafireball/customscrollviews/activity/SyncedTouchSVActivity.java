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
public class SyncedTouchSVActivity extends RoboSherlockFragmentActivity {

    @InjectView(R.id.BTN_scroll_left_to_top) Button scrollLeftToTopBTN;
    @InjectView(R.id.SVleft) SyncedScrollView leftSV;
    @InjectView(R.id.SVright) SyncedScrollView rightSV;
    @InjectView(R.id.CB_lock1) CheckBox lock1CB;
    @InjectView(R.id.CB_lock2) CheckBox lock2CB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MainApp.TAG, "onCreate");
        setContentView(R.layout.synced_touch_sv);

        scrollLeftToTopBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftSV.scrollTo(0, 0);
            }
        });

        lock1CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                leftSV.setDispatchScroll(b);
                leftSV.setDispatchTouch(b);
            }
        });

        lock2CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rightSV.setDispatchScroll(b);
                rightSV.setDispatchTouch(b);
            }
        });
    }
}
