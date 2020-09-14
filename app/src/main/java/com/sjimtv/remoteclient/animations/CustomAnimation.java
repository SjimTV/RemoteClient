package com.sjimtv.remoteclient.animations;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

public class CustomAnimation {

    private static final int MENU_DURATION = 500;
    private static final Interpolator MENU_INTERPOLATOR = new AccelerateDecelerateInterpolator();

    public static Animation slideMenuInLeft(){
        Animation slideInLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);

        slideInLeft.setFillAfter(true);
        slideInLeft.setDuration(MENU_DURATION);
        slideInLeft.setInterpolator(MENU_INTERPOLATOR);
        return slideInLeft;
    }

    public static Animation slideMenuOutLeft(){
        Animation slideOutLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        slideOutLeft.setDuration(MENU_DURATION);
        slideOutLeft.setInterpolator(MENU_INTERPOLATOR);
        return slideOutLeft;
    }

    public static Animation fadeMenuOut() {
        Animation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setDuration(MENU_DURATION);
        fadeOut.setInterpolator(MENU_INTERPOLATOR);
        return fadeOut;
    }

    public static Animation fadeMenuIn() {
        Animation fadeOut = new AlphaAnimation(0f, 1f);
        fadeOut.setDuration(MENU_DURATION);
        fadeOut.setInterpolator(MENU_INTERPOLATOR);
        return fadeOut;
    }
}
