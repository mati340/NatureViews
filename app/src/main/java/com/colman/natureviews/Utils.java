package com.colman.natureviews;

import android.widget.ImageView;

public class Utils {

    public static void animateBackground(ImageView background){
        background.animate().scaleX((float) 1.5).scaleY((float) 1.5).setDuration(20000).start();
    }
}