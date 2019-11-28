package com.enfin.ofabee3.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.res.ResourcesCompat;

import com.enfin.ofabee3.R;

public class AvenirRadioButton extends AppCompatRadioButton {

    public AvenirRadioButton(Context context) {
        super(context);
        init();
    }

    public AvenirRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvenirRadioButton(Context context, AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = ResourcesCompat.getFont(getContext(), R.font.avenir_medium);
            setTypeface(tf);
        }
    }
}
