package com.drughub.citizen.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

public class DrughubIcon extends TextView {

    private static Typeface sDrughubIcons = null;

    public DrughubIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DrughubIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrughubIcon(Context context) {
        super(context);
        init();
    }

    private void init() {
        if(sDrughubIcons == null)
            sDrughubIcons = Typeface.createFromAsset(getContext().getAssets(), "fonts/drughub-mobile-doctor.ttf");
        setTypeface(sDrughubIcons);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if(params != null)
            params.height = (int) getTextSize() + getPaddingTop() + getPaddingBottom();

        super.setLayoutParams(params);
    }

    public static Typeface getsDrughubIcons(Context context) {
        if(sDrughubIcons==null)
            sDrughubIcons = Typeface.createFromAsset(context.getAssets(), "fonts/drughub-mobile-doctor.ttf");
        return sDrughubIcons;
    }
}
