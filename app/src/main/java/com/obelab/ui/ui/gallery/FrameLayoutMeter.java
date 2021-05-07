package com.obelab.ui.ui.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FrameLayoutMeter extends FrameLayout {
    ResultMeterView mResultmeterView;

    public FrameLayoutMeter(@NonNull Context context) {
        super(context);
        init();
    }

    public FrameLayoutMeter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FrameLayoutMeter(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FrameLayoutMeter(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    private void init() {
        mResultmeterView = new ResultMeterView(getContext());
        addView(mResultmeterView);
        requestLayout();
        mResultmeterView.invalidate();
    }

    public void setValue(int value) {
        // mResultmeterView.setValue(value);
    }
}
