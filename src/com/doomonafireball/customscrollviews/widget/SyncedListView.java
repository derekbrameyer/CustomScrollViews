package com.doomonafireball.customscrollviews.widget;

import com.doomonafireball.customscrollviews.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class SyncedListView extends ListView implements SyncedListContainer.TouchNotifier, SyncedListContainer.SelectionNotifier {

    private SyncedListContainer.TouchListener mTouchListener = null;
    private SyncedListContainer.SelectionListener mSelectionListener = null;
    private boolean mDispatchTouch = true;
    private boolean mDispatchSelection = true;

    public SyncedListView(Context context) {
        super(context);
    }

    public SyncedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SyncedScrollView, 0, 0);
        mDispatchTouch = a.getBoolean(R.styleable.SyncedScrollView_dispatchTouch, true);
        mDispatchSelection = a.getBoolean(R.styleable.SyncedScrollView_dispatchSelection, true);
    }

    public SyncedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SyncedScrollView, defStyle, 0);
        mDispatchTouch = a.getBoolean(R.styleable.SyncedScrollView_dispatchTouch, true);
        mDispatchSelection = a.getBoolean(R.styleable.SyncedScrollView_dispatchSelection, true);
    }

    public void setDispatchTouch(boolean b) {
        this.mDispatchTouch = b;
    }

    public boolean getDispatchTouch() {
        return this.mDispatchTouch;
    }

    public void setDispatchSelection(boolean b) {
        this.mDispatchSelection = b;
    }

    public boolean getDispatchSelection() {
        return this.mDispatchSelection;
    }

    @Override
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
        if (mSelectionListener != null && mDispatchSelection) {
            mSelectionListener.smoothScrollToPosition(this, position);
        }
    }

    @Override
    public void smoothScrollByOffset(int offset) {
        super.smoothScrollByOffset(offset);
        if (mSelectionListener != null && mDispatchSelection) {
            mSelectionListener.smoothScrollByOffset(this, offset);
        }
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (mSelectionListener != null && mDispatchSelection) {
            mSelectionListener.setSelection(this, position);
        }
    }

    @Override
    public void setSelectionFromTop(int position, int y) {
        super.setSelectionFromTop(position, y);
        if (mSelectionListener != null && mDispatchSelection) {
            mSelectionListener.setSelectionFromTop(this, position, y);
        }
    }

    @Override
    public void setSelectionAfterHeaderView() {
        super.setSelectionAfterHeaderView();
        if (mSelectionListener != null && mDispatchSelection) {
            mSelectionListener.setSelectionAfterHeaderView(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        if (mTouchListener != null && mDispatchTouch) {
            mTouchListener.onTouchEvent(this, ev);
        }
        return true;
    }

    @Override
    public void setTouchListener(SyncedListContainer.TouchListener touchListener) {
        this.mTouchListener = touchListener;
    }

    @Override
    public SyncedListContainer.TouchListener getTouchListener() {
        return mTouchListener;
    }

    @Override
    public void setSelectionListener(SyncedListContainer.SelectionListener selectionListener) {
        this.mSelectionListener = selectionListener;
    }

    @Override
    public SyncedListContainer.SelectionListener getSelectionListener() {
        return mSelectionListener;
    }
}