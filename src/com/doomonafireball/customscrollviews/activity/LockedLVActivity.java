package com.doomonafireball.customscrollviews.activity;

import com.doomonafireball.customscrollviews.MainApp;
import com.doomonafireball.customscrollviews.R;
import com.doomonafireball.customscrollviews.widget.SyncedListView;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import roboguice.inject.InjectView;

/**
 * User: derek Date: 6/29/12 Time: 4:01 PM
 */
public class LockedLVActivity extends RoboSherlockFragmentActivity {

    @InjectView(R.id.BTN_smooth_scroll_left_to_top) Button smoothScrollLeftToTopBTN;
    @InjectView(R.id.BTN_smooth_scroll_by_offset) Button smoothScrollByOffsetBTN;
    @InjectView(R.id.BTN_set_left_selection) Button setLeftSelectionBTN;
    @InjectView(R.id.BTN_set_left_selection_from_top) Button setLeftSelectionFromTopBTN;
    @InjectView(R.id.LV_left) SyncedListView leftLV;
    @InjectView(R.id.LV_right) SyncedListView rightLV;
    @InjectView(R.id.CB_lock1) CheckBox lock1CB;
    @InjectView(R.id.CB_lock2) CheckBox lock2CB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MainApp.TAG, "onCreate");
        setContentView(R.layout.locked_lv);

        leftLV.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.string_array_1)));
        rightLV.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.string_array_2)));

        smoothScrollLeftToTopBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftLV.smoothScrollToPosition(0);
            }
        });

        smoothScrollByOffsetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftLV.smoothScrollByOffset(2);
            }
        });

        setLeftSelectionBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftLV.setSelection(10);
            }
        });

        setLeftSelectionFromTopBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftLV.setSelectionFromTop(10, 5);
            }
        });

        lock1CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                leftLV.setDispatchTouch(b);
                leftLV.setDispatchSelection(b);
            }
        });

        lock2CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rightLV.setDispatchTouch(b);
                rightLV.setDispatchSelection(b);
            }
        });
    }
}
