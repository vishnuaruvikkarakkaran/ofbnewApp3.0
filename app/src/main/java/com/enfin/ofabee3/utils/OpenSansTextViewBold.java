package com.enfin.ofabee3.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class OpenSansTextViewBold extends OpenSansTextView {

    public OpenSansTextViewBold(Context context) {
        super(context);
        init();
    }

    public OpenSansTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenSansTextViewBold(Context context, AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Bold.ttf");
            setTypeface(tf);
        }
    }

    public void strikeText(){
        this.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

}