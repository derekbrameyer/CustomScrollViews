package com.doomonafireball.customscrollviews.activity;

import com.doomonafireball.customscrollviews.MainApp;
import com.doomonafireball.customscrollviews.R;
import com.doomonafireball.customscrollviews.widget.FrzColumnListView;
import com.doomonafireball.customscrollviews.widget.FrzMainListView;
import com.doomonafireball.customscrollviews.widget.SyncedListView;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * User: derek Date: 7/20/12 Time: 10:20 AM
 */
public class ListView2DActivity extends RoboSherlockFragmentActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MainApp.TAG, "onCreate");
        setContentView(R.layout.list_view_2d);

        ArrayList<String> listItems = new ArrayList<String>();
        ArrayList<String> frozenColumnItems = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            listItems.add("Mercury Venus Earth Mars Jupiter Saturn Uranus Neptune Ceres Pluto Haumea  Makemake Eris This That Space Pole");
            frozenColumnItems.add("Row" + i);
        }
        FrzMainListView listView = (FrzMainListView) findViewById(R.id.LV_demo);
        FrzColumnListView frozenColumnListView = (FrzColumnListView) findViewById(R.id.LV_demo_frozen_column);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.list_item, listItems);
        ArrayAdapter<String> frozenColumnListAdapter = new ArrayAdapter<String>(this, R.layout.list_item, frozenColumnItems);
        listView.setAdapter(listAdapter);
        frozenColumnListView.setAdapter(frozenColumnListAdapter);
    }

}
