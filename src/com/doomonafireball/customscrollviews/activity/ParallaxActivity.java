package com.doomonafireball.customscrollviews.activity;

import com.doomonafireball.customscrollviews.R;
import com.doomonafireball.customscrollviews.widget.SyncedScrollManager;
import com.doomonafireball.customscrollviews.widget.SyncedScrollView;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

import android.os.Bundle;

import roboguice.inject.InjectView;

/**
 * User: derek Date: 6/29/12 Time: 12:38 PM
 */
public class ParallaxActivity extends RoboSherlockFragmentActivity {

    @InjectView(R.id.SSV_1) private SyncedScrollView ssv1;
    @InjectView(R.id.SSV_2) private SyncedScrollView ssv2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parallax);

        SyncedScrollManager ssm = new SyncedScrollManager(true, 0.5f);
        ssm.addScrollClient(ssv1);
        ssm.addScrollClient(ssv2);
    }
}
