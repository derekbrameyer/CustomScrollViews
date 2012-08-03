package com.doomonafireball.customscrollviews.activity;

import com.doomonafireball.customscrollviews.MainApp;
import com.doomonafireball.customscrollviews.R;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import roboguice.inject.InjectView;

/**
 * User: derek Date: 6/29/12 Time: 4:03 PM
 */
public class DemoActivity extends RoboSherlockFragmentActivity {

    String[] activityNames = new String[]{
            "Two Parallax ScrollViews",
            "Three Parallax ScrollViews",
            "Locked ScrollViews",
            "Synced-Scroll ScrollViews",
            "Unsynced-Scroll ScrollViews",
            "Synced-Touch ScrollViews",
            "Locked ListViews",
            "Two-Dimensional ScrollView",
            "Complex Two-Dimensional ScrollView",
            "Two-Dimensional ListView",
            "Max Scroll ScrollView"
    };
    Class[] activityClasses = new Class[]{
            ParallaxTwoSVActivity.class,
            ParallaxThreeSVActivity.class,
            LockedSVActivity.class,
            SyncedSVActivity.class,
            UnsyncedSVActivity.class,
            SyncedTouchSVActivity.class,
            LockedLVActivity.class,
            ScrollView2DActivity.class,
            ScrollView2DComplexActivity.class,
            ListView2DActivity.class,
            MaxScrollScrollViewActivity.class
    };

    @InjectView(R.id.LV_demo) ListView demoLV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MainApp.TAG, "onCreate");
        setContentView(R.layout.demo);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activityNames);
        demoLV.setAdapter(aa);
        demoLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startActivity(new Intent(DemoActivity.this, activityClasses[position]));
            }
        });
    }

}
