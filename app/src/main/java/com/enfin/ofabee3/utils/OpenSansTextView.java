package com.enfin.ofabee3.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class OpenSansTextView extends AppCompatTextView {

    public OpenSansTextView(Context context) {
        super(context);
        init();
    }

    public OpenSansTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenSansTextView(Context context, AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
            setTypeface(tf);
        }
    }

    public void strikeText(){
        this.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

}