package com.doomonafireball.customscrollviews.activity;

import com.doomonafireball.customscrollviews.MainApp;
import com.doomonafireball.customscrollviews.R;
import com.doomonafireball.customscrollviews.widget.FreezableTwoDScrollView;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import roboguice.inject.InjectView;

/**
 * User: derek Date: 7/16/12 Time: 3:51 PM
 */
public class ScrollView2DComplexActivity extends RoboSherlockFragmentActivity {

    //@InjectView(R.id.pin_1) View pin1;
    //@InjectView(R.id.pin_2) View pin2;
    //@InjectView(R.id.pin_3) View pin3;
    //@InjectView(R.id.header_1) View header1;
    //@InjectView(R.id.header_2) View header2;
    //@InjectView(R.id.header_3) View header3;
    //@InjectView(R.id.vertical) PinnedHeaderScrollView vertical;

    //@InjectView(R.id.h_pin_1) View hPin1;
    //@InjectView(R.id.h_header_1) View hHeader1;
    //@InjectView(R.id.twoD) FreezableTwoDScrollView twoD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MainApp.TAG, "onCreate");
        setContentView(R.layout.scroll_view_2d_complex);

        /*vertical.addPinView(pin1);
        vertical.addHeaderView(header1);
        /*leftSV.addPinView(pin2);
        leftSV.addHeaderView(header2);
        leftSV.addPinView(pin3);
        leftSV.addHeaderView(header3);*/

        /*twoD.addPinView(hPin1);
        twoD.addHeaderView(hHeader1); */
    }
}
