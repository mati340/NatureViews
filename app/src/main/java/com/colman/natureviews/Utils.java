package com.colman.natureviews;

import android.widget.ImageView;


public class Utils {

    static int REQUEST_CODE = 1;

    public static void animateBackground(ImageView background, long duration){
        background.animate().scaleX((float) 1.5).scaleY((float) 1.5).setDuration(duration).start();
    }

}
